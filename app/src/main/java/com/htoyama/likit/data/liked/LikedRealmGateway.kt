package com.htoyama.likit.data.liked

import android.util.LongSparseArray
import com.htoyama.likit.data.liked.tweet.cache.TweetMapper
import com.htoyama.likit.data.tag.TagMapper
import com.htoyama.likit.domain.liked.LikedTweet
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tweet.Tweet
import io.realm.Realm
import io.realm.RealmList
import java.util.*
import javax.inject.Inject

/**
 * A gateway that handles liked-tweet and tag list via Realm.
 */
open class LikedRealmGateway @Inject internal constructor(
    private val tweetMapper: TweetMapper,
    private val tagMapper: TagMapper) {

  /**
   * Insert the given list as Liked which contains no tag.
   *
   * Note: If the Cache already havs a tweet in the given list,
   * the cache ignore the tweet data instead of updating.
   */
  open fun insertAsContainingNoTag(tweetList: List<Tweet>) {
    Realm.getDefaultInstance().use {
      it.executeTransaction {
        val likedList = tweetList.map { RealmLikedTweet(tweetMapper.mapFrom(it), RealmList()) }
        it.insertOrUpdate(likedList)
      }
    }
  }

  /**
   * Retrieve some Likes as [LongSparseArray]
   * Its key means tweet-id and its value means [Tag] list.
   *
   * The result is ordered by tweet-id descending.
   */
  open fun getBy(tweetList: List<Tweet>): List<LikedTweet> {
    Realm.getDefaultInstance().use {
      val size = tweetList.size - 1
      val query = it.where(RealmLikedTweet::class.java)
      tweetList.forEachIndexed { index, tweet ->
        if (index < size) {
          query.equalTo("tweet.id", tweet.id)
              .or()
        } else {
          query.equalTo("tweet.id", tweet.id)
        }
      }

      //val all = query.findAllSorted("tweetId", Sort.DESCENDING)
      val all = query.findAll()
      val table = ArrayList<LikedTweet>(all.size)
      all.forEach {
        val tagList = it.tagList.map { tagMapper.mapFrom(it) }
        table.add(LikedTweet(
            tweetMapper.mapFrom(it.tweet),
            tagList
        ))
      }

      return table
    }

  }

  /**
   * Retrieve tweet count associated by a given [Tag]
   */
  open fun getTweetCountBy(tag: Tag): Int {
    Realm.getDefaultInstance().use { realm ->
      return realm.where(RealmLikedTweet::class.java)
          .equalTo("tagList.id", tag.id)
          .findAll()
          //.findAllSorted("tweet.id")
          .size
    }
  }

}
