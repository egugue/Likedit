package com.htoyama.likit.data.liked

import android.util.LongSparseArray

import com.htoyama.likit.domain.liked.LikedTweet
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tweet.Tweet
import java.util.*
import javax.inject.Inject

/**
 * A factory that builds [LikedTweet]
 */
class LikedFactory
    @Inject constructor() {

  /**
   * Create [LikedTweet]s list by the given params.
   */
  fun createFrom(tagTable: LongSparseArray<List<Tag>>,
                 tweetList: List<Tweet>): List<LikedTweet> {
    val tweetTable = LongSparseArray<Tweet>(tweetList.size)
    tweetList.forEach {
      tweetTable.put(it.id, it)
    }

    val likedList = ArrayList<LikedTweet>(tagTable.size())
    val size = tagTable.size() - 1
    for (index in size downTo 0) {
      val tweetId = tagTable.keyAt(index)
      val tweet: Tweet? = tweetTable.get(tweetId)

      if (tweet != null) {
        val tagList = tagTable.valueAt(index)
        likedList.add(LikedTweet(
            tweet = tweet,
            tagList = tagList
        ))
      }
    }

    return likedList
  }
}