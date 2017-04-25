package com.htoyama.likit.data.sqlite.tweet

import com.htoyama.likit.data.sqlite.LikedTweetModel
import com.htoyama.likit.data.sqlite.user.UserEntity

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
  }
}