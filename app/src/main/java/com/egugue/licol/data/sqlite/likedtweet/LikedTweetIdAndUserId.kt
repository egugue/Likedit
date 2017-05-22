package com.egugue.licol.data.sqlite.likedtweet

import com.egugue.licol.data.sqlite.LikedTweetModel

data class LikedTweetIdAndUserId(val likedTweetId: Long, val userId: Long)
  : LikedTweetModel.Select_id_by_user_idsModel {
  override fun id(): Long = likedTweetId
  override fun user_id(): Long = userId

  companion object {
    val MAPPER = LikedTweetEntity.FACTORY.select_id_by_user_idsMapper(::LikedTweetIdAndUserId)!!
  }
}
