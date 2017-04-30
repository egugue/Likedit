package com.htoyama.likit.data.sqlite.likedtweet

import com.htoyama.likit.data.sqlite.IdMap
import com.htoyama.likit.data.sqlite.LikedTweetModel
import com.htoyama.likit.data.sqlite.user.UserEntity
import com.htoyama.likit.data.sqlite.user.toUser
import com.htoyama.likit.domain.likedtweet.LikedTweet
import com.htoyama.likit.domain.tweet.Tweet

/**
 * An entity which has both [LikedTweetEntity] and [UserEntity]
 */
data class FullLikedTweetEntity(
    val tweet: LikedTweetEntity,
    val user: UserEntity
) : LikedTweetModel.Select_allModel<LikedTweetEntity, UserEntity> {

  override fun liked_tweet(): LikedTweetEntity = tweet
  override fun user(): UserEntity = user

  companion object {
    val MAPPER = LikedTweetEntity.FACTORY.select_allMapper(::FullLikedTweetEntity, UserEntity.FACTORY)!!

    fun fromLikedTweet(l: LikedTweet): FullLikedTweetEntity {
      val tweet = l.tweet
      val user = tweet.user

      return FullLikedTweetEntity(
          LikedTweetEntity(
              tweet.id,
              tweet.user.id,
              tweet.text,
              tweet.photoList,
              tweet.urlList,
              tweet.video,
              tweet.createdAt
          ),
          UserEntity(
              user.id,
              user.name,
              user.screenName,
              user.avatorUrl
          )
      )

    }
  }
}

fun FullLikedTweetEntity.toLikedTweet(idMap: IdMap) =
    LikedTweet(
        Tweet(
            tweet.id,
            user.toUser(),
            tweet.created,
            tweet.text,
            tweet.imageList,
            tweet.urlList,
            tweet.video
        ),
        idMap.getOrEmptyList(tweet.id)
    )