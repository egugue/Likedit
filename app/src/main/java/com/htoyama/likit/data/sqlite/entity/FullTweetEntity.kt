package com.htoyama.likit.data.sqlite.entity

import com.htoyama.likit.data.sqlite.TweetModel

/**
 * An entity which has both [TweetEntity] and [UserEntity]
 */
data class FullTweetEntity(
    val tweet: TweetEntity,
    val user: UserEntity
) : TweetModel.Select_allModel<TweetEntity, UserEntity> {

  override fun tweet(): TweetEntity = tweet
  override fun user(): UserEntity = user
}