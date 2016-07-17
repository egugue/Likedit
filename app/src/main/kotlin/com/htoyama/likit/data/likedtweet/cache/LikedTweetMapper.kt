package com.htoyama.likit.data.likedtweet.cache

import com.htoyama.likit.data.likedtweet.RealmLikedTweet
import com.htoyama.likit.data.likedtweet.tweet.cache.TweetMapper
import com.htoyama.likit.data.tag.RealmTag
import com.htoyama.likit.data.tag.TagMapper
import com.htoyama.likit.domain.likedtweet.LikedTweet
import io.realm.RealmList
import javax.inject.Inject

/**
 * Transform between [LikedTweet] and [RealmLikedTweet].
 */
internal class LikedTweetMapper
    @Inject constructor(private val tweetMapper: TweetMapper,
                        private val tagMapper: TagMapper) {

  /**
   * Transform [LikedTweet] into [RealmLikedTweet]
   */
  /*
  fun mapFrom(liked: LikedTweet): RealmLikedTweet {
    val realmTweet = tweetMapper.mapFrom(liked.tweet)
    val tagList = RealmList<RealmTag>()
    liked.tagList.forEach {
      tagList.add(tagMapper
          .mapFrom(it))
    }

    return RealmLikedTweet(
        tweetId = realmTweet,
        tagList = tagList)
  }
  */

  /**
   * Transform [RealmLikedTweet] into [LikedTweet]
   */
  /*
  fun mapFrom(realmLiked: RealmLikedTweet): LikedTweet {
    val tweet = tweetMapper.mapFrom(realmLiked.tweetId)
    val tagList = realmLiked.tagList.map {
      tagMapper.mapFrom(it)
    }

    return LikedTweet(
        tweet = tweet,
        tagList = tagList
    )
  }
  */

}