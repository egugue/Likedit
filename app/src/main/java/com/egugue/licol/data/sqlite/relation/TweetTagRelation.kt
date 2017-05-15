package com.egugue.licol.data.sqlite.relation

import com.egugue.licol.data.sqlite.TweetTagRelationModel

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