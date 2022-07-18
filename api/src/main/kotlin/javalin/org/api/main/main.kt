package org.api.main

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.security.RouteRole
import org.unq.ui.bootstrap.getTwitterSystem
import io.javalin.core.util.RouteOverviewPlugin
import org.api.controllers.TweetController
import org.api.controllers.TwitterController
import org.api.controllers.UserController
import org.api.token.TokenAccessManager
import token.JwtController

enum class Roles: RouteRole {
    ANYONE,USER
}

class TwitterApi {

    fun start() {
        val system = getTwitterSystem()
        val twitterController = TwitterController(system)
        val userController = UserController(system)
        val tweetController = TweetController(system)

        val app = Javalin.create {
            it.defaultContentType = "application/json"
            it.registerPlugin(RouteOverviewPlugin("/routes"))
            it.accessManager(TokenAccessManager(JwtController(system)))
            it.enableCorsForAllOrigins()
        }

        app.before {
            it.header("Access-Control-Expose-Headers", "*")
        }

        app.start(8080)

        app.routes {
            path("register") {
                post(userController::register, Roles.ANYONE)
            }
            path("login") {
                post(userController::login, Roles.ANYONE)
            }
            path("user") {
                get(userController::get, Roles.USER)
                post(userController::postEditProfile, Roles.USER)
                path("{userId}") {
                    get(userController::getById, Roles.USER)
                    path("follow") {
                        put(userController::putByFollow, Roles.USER)
                    }
                }
            }
            path("tweet") {
                post(tweetController::addTweet, Roles.USER)
                path("{tweetId}") {
                    get(tweetController::getById, Roles.USER)
                    delete(tweetController::deleteTweet, Roles.USER)
                    path("like") {
                        put(tweetController::putLike,Roles.USER)
                    }
                    path("comment") {
                        post(tweetController::postComment, Roles.USER)
                    }
                }
            }
            path("search") {
                get(twitterController::getSearch, Roles.ANYONE)
            }
        }
    }
}

fun main() {
    TwitterApi().start()
}