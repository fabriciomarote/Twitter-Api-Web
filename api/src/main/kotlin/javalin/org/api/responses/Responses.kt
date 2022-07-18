package org.api

import org.unq.ui.model.Tweet
import org.unq.ui.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class OkResponse(val result: String = "ok")

data class ErrorResponse(val result: String)

data class SearchResponse(val content : List<Any>)

data class LoginDTO(val email: String, val password: String)

data class UserSimpleDTO(val name: String, val email: String, val password: String, val image: String)

data class UserEditDTO(val name: String, val password: String, val image: String)

data class UserResponseDTO(val id: String, val name: String, val image: String, val followers: List<SimpleUserDTO>, val timeline: List<TweetDTO>)

data class UserByIdResponse(val name: String,
                            val image: String,
                            val followers: List<SimpleUserDTO>,
                            val tweets: List<TweetDTO>)

data class UserWithFollowersResponse(val id: String,
                                     val name: String,
                                     val image: String,
                                     val followers: List<SimpleUserDTO>)

data class TweetDTO(val id: String, val text: String, val images: MutableList<String>, val reply: SimpleTweetDTO?, val likes: List<SimpleUserDTO>, val date: String, val author: SimpleUserDTO, val comment: List<SimpleCommentDTO>)

data class TweetCommentDTO(val id: String,
                           val text: String,
                           val images: MutableList<String>,
                           val reply: SimpleTweetDTO?,
                           val likes: List<SimpleUserDTO>,
                           val date: String,
                           val author: SimpleUserDTO,
                           val comment: List<SimpleCommentDTO>)

data class SimpleUserDTO(val id: String,
                         val name: String,
                         val image: String)

data class SimpleTweetDTO(val id: String,
                          val text: String,
                          val images: MutableList<String>,
                          val author: SimpleUserDTO)

data class SimpleTweetWithLikes(val id: String,
                                val text: String,
                                val images: MutableList<String>,
                                val likes: List<SimpleUserDTO>,
                                val date: String,
                                val author: SimpleUserDTO)

data class SimpleCommentDTO(val id: String,
                            val text: String,
                            val images: MutableList<String>,
                            val author: SimpleUserDTO,
                            val reply: SimpleTweetDTO?,
                            val likes: List<SimpleUserDTO>,
                            val comment: List<SimpleCommentDTO>)

class Transform(){
    companion object transformer{

        fun userToSimpleUser(user: User): SimpleUserDTO{
            return SimpleUserDTO(user.id, user.name, user.image)
        }

        fun tweetToSimpleTweet(tweet: Tweet): SimpleTweetDTO {
            return SimpleTweetDTO(tweet.id, tweet.text, tweet.images, userToSimpleUser(tweet.author))
        }

        fun likesToSimpleUsers(likes: MutableList<User>): List<SimpleUserDTO>{
            val likesTransformed = likes.map{u -> SimpleUserDTO(u.id, u.name, u.image)}
            return likesTransformed
        }

        fun commentsToSimpleComments(comments: MutableList<Tweet>): List<SimpleCommentDTO> {
            val commentsTransformed = comments.map { c -> SimpleCommentDTO(c.id, c.text, c.images, userToSimpleUser(c.author), replyToSimpleTweet(c.reply), likesToSimpleUsers(c.likes), commentsToSimpleComments(c.comments))}
            return commentsTransformed
        }

        fun dateToFormattedDate(date: LocalDateTime): String {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")
            val formatDateTime = LocalDateTime.parse(date.toString())
            return formatDateTime.format(formatter).toString()
        }

        fun replyToSimpleTweet(reply: Tweet?): SimpleTweetDTO?{
            val response =
                if (reply != null){
                tweetToSimpleTweet(reply)
                }else{
                    null
                }
            return response
        }

        fun followersToSimpleUsers(followers: List<User>): List<SimpleUserDTO>{
            val followersTransformed = followers.map {
                SimpleUserDTO(it.id, it.name, it.image)
            }
            return followersTransformed
        }

        fun listTweetToTweetResponse(list: List<Tweet>): List<TweetDTO>{
            return list.map{
                TweetDTO(it.id, it.text, it.images, replyToSimpleTweet(it.reply), likesToSimpleUsers(it.likes),
                dateToFormattedDate(it.date), userToSimpleUser(it.author), commentsToSimpleComments(it.comments))}
        }
    }
}