package com.htoyama.likit.data.sqlite.entity

import com.htoyama.likit.data.sqlite.TweetTagRelationModel

data class TweetTagRelation(
    val tweetId: Long,
    val tagId: Long
) : TweetTagRelationModel {

  override fun tweet_id(): Long = tweetId
  override fun tag_id(): Long = tagId

  companion object {
    val FACTORY = TweetTagRelationModel.Factory(::TweetTagRelation)
  }
}