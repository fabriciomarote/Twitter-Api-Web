package org.api.controllers

import io.javalin.http.Context
import io.javalin.http.UnauthorizedResponse
import org.api.*
import org.unq.ui.model.NotFound
import org.unq.ui.model.TwitterSystem
import org.unq.ui.model.UsedEmail
import org.unq.ui.model.User
import token.JwtController

class UserController(private val system: TwitterSystem) {

    private val tokenJWT = JwtController(system)

    fun register(ctx: Context){
        val body = ctx.bodyValidator<UserSimpleDTO>()
                .check({ it.name.isNotEmpty() }, "Name cannot be empty")
                .check({ it.email.isNotEmpty() }, "Email cannot be empty")
                .check({
                    "^[a-zA-Z0-9.!#$%&'+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)$"
                            .toRegex()
                            .matches(it.email)
                }, "Invalid email address")
                .check({ it.password.isNotEmpty() }, "Password cannot be empty")
                .check({ it.image.isNotEmpty() }, "Image cannot be empty")
                .get()
        try {
            system.register(body.name, body.email, body.password, body.image)
            ctx.json(OkResponse())
        } catch (e: UsedEmail){
            ctx.status(404).json(ErrorResponse("The e-mail is not available"))
        }
    }

    fun login(ctx: Context){
        val body = ctx.bodyValidator<LoginDTO>()
                .check({ it.email.isNotEmpty() }, "Email cannot be empty")
                .check({
                    "^[a-zA-Z0-9.!#$%&'+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)$"
                            .toRegex()
                            .matches(it.email)
                }, "Invalid email address")
                .check({ it.password.isNotEmpty() }, "Password cannot by empty")
                .get()
        try {
            val user = system.login(body.email, body.password)
            ctx.header("Authorization", tokenJWT.generate(user)).json(OkResponse())
        } catch (e: NotFound){
            ctx.status(404).json(mapOf("message" to e.message))
        }
    }

    fun get(ctx: Context){
        val token = ctx.header("Authorization")
        val user = tokenJWT.validate(token!!)
        val userResponse = UserResponseDTO(user.id, user.name, user.image, Transform.followersToSimpleUsers(user.followers), Transform.listTweetToTweetResponse(system.timeline(user.id)))
        ctx.json(userResponse)
    }

    fun getById(ctx: Context){
        val id = ctx.pathParam("{userId}")
        val user: User
        try {
            user = system.getUser(id)
            val userByIdResponse = UserByIdResponse(user.name, user.image, Transform.followersToSimpleUsers(user.followers), Transform.listTweetToTweetResponse(user.tweets))
            ctx.json(userByIdResponse)
        } catch (e: NotFound){
            ctx.status(404).json(mapOf("message" to e.message))
        }
    }

    fun putByFollow(ctx: Context){
        val toFollowId = ctx.pathParam("{userId}")
        val followerId = getUserId(ctx)
        try{
            system.updateFollower(toFollowId, followerId)
            ctx.json(OkResponse())
        } catch (e: NotFound){
            ctx.status(404).json(mapOf("message" to e.message))
        }
    }

    fun postEditProfile(ctx: Context) {
        val userId = getUserId(ctx)
        val user: User
        try {
        user = system.getUser(userId)
        val body = ctx.bodyValidator<UserEditDTO>()
            .check({ it.name.isNotEmpty() }, "Name cannot be empty")
            .check({ it.password.isNotEmpty() }, "Password cannot by empty")
            .get()
            system.editProfile(userId, body.name.ifEmpty { user.name }, body.password.ifEmpty(){ user.password }, body.image.ifEmpty { user.image })
            ctx.json(OkResponse())
        } catch (e: NotFound) {
            ctx.status(404).json(mapOf("message" to e.message))
        }
    }

    private fun getUserId(ctx: Context) : String {
        val id : String? = ctx.attribute("id")
        if (id == null) {
            UnauthorizedResponse()
        }
        return id!!
    }
}