package com.htoyama.likit.data.sqlite.tweet

import com.htoyama.likit.data.sqlite.TweetModel
import com.htoyama.likit.data.sqlite.user.UserEntity

/**
 * An entity which has both [TweetEntity] and [UserEntity]
 */
data class FullTweetEntity(
    val tweet: TweetEntity,
    val user: UserEntity
) : TweetModel.Select_allModel<TweetEntity, UserEntity> {

  override fun tweet(): TweetEntity = tweet
  override fun user(): UserEntity = user

  companion object {
    val MAPPER = TweetEntity.FACTORY.select_allMapper(::FullTweetEntity, UserEntity.FACTORY)!!
  }
}