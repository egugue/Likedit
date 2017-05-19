package com.egugue.licol.data.sqlite.likedtweet

import com.egugue.licol.data.sqlite.QuotedTweetModel
import com.egugue.licol.domain.likedtweet.QuotedTweet

/**
 * Created by htoyama on 2017/05/19.
 */
data class QuotedTweetEntity(
    val id: Long,
    val text: String,
    val userId: Long,
    val userName: String,
    val userScreenName: String,
    val userAvatarUrl: String
) : QuotedTweetModel {

  override fun id(): Long = id
  override fun text(): String = text
  override fun user_id(): Long = userId
  override fun user_name(): String = userName
  override fun user_screen_name(): String = userScreenName
  override fun user_avatar_url(): String = userAvatarUrl

  fun toQuotedTweet(): QuotedTweet = QuotedTweet(
      id,
      text,
      userId,
      userName,
      userScreenName,
      userAvatarUrl
  )

  companion object {
    val FACTORY = QuotedTweetModel.Factory<QuotedTweetEntity>(::QuotedTweetEntity)

    fun from(q: QuotedTweet?): QuotedTweetEntity? {
      if (q == null) return null

      return QuotedTweetEntity(
          q.id,
          q.text,
          q.userId,
          q.userName,
          q.userName,
          q.userScreenName
      )
    }

  }
}