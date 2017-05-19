package com.egugue.licol.data.sqlite.likedtweet

import com.egugue.licol.data.sqlite.IdMap
import com.egugue.licol.data.sqlite.LikedTweetModel
import com.egugue.licol.domain.likedtweet.LikedTweet

/**
 * An entity which has both [LikedTweetEntity] and [QuotedTweetEntity]
 */
data class FullLikedTweetEntity(
    val tweet: LikedTweetEntity,
    val quoted: QuotedTweetEntity?
) : LikedTweetModel.Select_allModel<LikedTweetEntity, QuotedTweetEntity> {

  override fun liked_tweet(): LikedTweetEntity = tweet
  override fun quoted_tweet(): QuotedTweetEntity? = quoted

  fun toLikedTweet(idMap: IdMap): LikedTweet {
    return LikedTweet(
        tweet.id,
        tweet.userId,
        tweet.created,
        tweet.text,
        tweet.likedCount,
        tweet.imageList,
        tweet.urlList,
        tweet.video,
        quoted?.toQuotedTweet(),
        idMap.getOrEmptyList(tweet.id)
    )
  }

  companion object {
    val MAPPER = LikedTweetEntity.FACTORY.select_allMapper(::FullLikedTweetEntity, QuotedTweetEntity.FACTORY)!!

    fun fromLikedTweet(l: LikedTweet): FullLikedTweetEntity {
      return FullLikedTweetEntity(
          LikedTweetEntity.fromLikedTweet(l),
          QuotedTweetEntity.from(l.quoted)
      )
    }
  }
}
