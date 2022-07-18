package org.api.controllers

import io.javalin.http.Context
import io.javalin.http.UnauthorizedResponse
import org.api.*
import org.unq.ui.model.*

class TweetController(private val system: TwitterSystem) {

    fun getById(ctx: Context) {
        val id = ctx.pathParam("{tweetId}")
        try {
            val tweet = system.getTweet(id)
            val tweetResponse = TweetCommentDTO(id,
                                                tweet.text,
                                                tweet.images,
                                                Transform.replyToSimpleTweet(tweet.reply),
                                                Transform.likesToSimpleUsers(tweet.likes),
                                                Transform.dateToFormattedDate(tweet.date),
                                                Transform.userToSimpleUser(tweet.author),
                                                Transform.commentsToSimpleComments(tweet.comments)
            )
            ctx.json(tweetResponse)
        } catch (e: NotFound) {
            ctx.status(404).json(mapOf("message" to e.message))
        }
    }

    fun putLike(ctx: Context){
        val id = ctx.pathParam("{tweetId}")
        val userId = getUserId(ctx)
        try {
            system.updateLike(id, userId)
            ctx.json(OkResponse())
        } catch (e: NotFound) {
            ctx.status(404).json(mapOf("message" to e.message))
        }
    }

    fun postComment(ctx: Context){
        val id = ctx.pathParam("{tweetId}")
        val userId = getUserId(ctx)
        val draft = ctx.bodyValidator<DraftTweet>()
                .check({this.validateImages(it.images)}, "Max of 4 images are allowed")
                .check({ it.text.isNotEmpty() || it.images.isNotEmpty()}, "Text and Images cannot be empty")
                .get()
        try {
            system.addComment(id, userId, draft)
            ctx.json(OkResponse())
        } catch (e: NotFound) {
           ctx.status(404).json(mapOf("message" to e.message))
        }
    }

    fun addTweet(ctx: Context) {
        val userId = getUserId(ctx)
        val draft = ctx.bodyValidator<DraftTweet>()
                .check({this.validateImages(it.images)}, "Max of 4 images are allowed")
                .check({ it.text.isNotEmpty() || it.images.isNotEmpty()}, "Text or Images cannot be empty")
                .get()
        try {
            system.addTweet(userId, draft)
            ctx.json(OkResponse())
        } catch (e: NotFound) {
            ctx.status(404).json(mapOf("message" to e.message))
        }
    }

    fun deleteTweet(ctx: Context) {
        val id = ctx.pathParam("{tweetId}")
        val userId = getUserId(ctx)
        try {
            val tweet = system.getTweet(id)
            if (tweet.author.id == userId) {
                system.deleteTweet(id)
                ctx.json(OkResponse())
            }
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

    private fun validateImages(images: MutableList<String>): Boolean {
        return images.size <= 4
    }
}
