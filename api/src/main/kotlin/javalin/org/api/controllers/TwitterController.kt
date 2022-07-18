package org.api.controllers

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import org.api.*
import org.unq.ui.model.TwitterSystem

class TwitterController(private val system: TwitterSystem) {

    fun getSearch(ctx: Context){
        val value = ctx.queryParam("q") ?: throw BadRequestResponse ("Nothimg to search")
        val search = if(hasHash(value!!)){
                SearchResponse(
                    system.searchByTag(value).map {
                        t ->
                        SimpleTweetWithLikes(
                            t.id,
                            t.text,
                            t.images,
                            Transform.likesToSimpleUsers(t.likes),
                            Transform.dateToFormattedDate(t.date),
                            Transform.userToSimpleUser(t.author))
                })
        } else {
            SearchResponse(
                system.searchByName(value).map { u ->
                    UserWithFollowersResponse(
                        u.id,
                        u.name,
                        u.image,
                        Transform.followersToSimpleUsers(u.followers)
                    )
                })
        }
        ctx.json(search)
    }

    fun hasHash(q: String): Boolean{
        return q.startsWith("#")
    }
}
