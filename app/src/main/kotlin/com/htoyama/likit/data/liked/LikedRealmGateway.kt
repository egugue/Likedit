package com.htoyama.likit.data.liked

import android.util.LongSparseArray
import com.htoyama.likit.data.tag.TagMapper
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tweet.Tweet
import io.realm.Realm
import io.realm.RealmList
import io.realm.Sort
import javax.inject.Inject

/**
 * A gateway that handles liked-tweet and tag list via Realm.
 */
open class LikedRealmGateway @Inject internal constructor(
    private val tagMapper: TagMapper) {

  /**
   * Insert the given list as Liked which contains no tag.
   *
   * Note: If the Cache already havs a tweet in the given list,
   * the cache ignore the tweet data instead of updating.
   */
  fun insertAsContainingNoTag(tweetList: List<Tweet>) {
    Realm.getDefaultInstance().use {
      it.executeTransaction {
        val likedList = tweetList.map { RealmLikedTweet(it.id, RealmList()) }
        it.insert(likedList)
      }
    }
  }

  /*
  fun getBy(tweetList: List<Tweet>): List<RealmLikedTweet> {
    Realm.getDefaultInstance().use {
      val size = tweetList.size - 1
      val query = it.where(RealmLikedTweet::class.java)
      tweetList.forEachIndexed { index, tweet ->
        if (index < size) {
          query.equalTo("tweetId", tweet.id)
              .or()
        } else {
          query.equalTo("tweetId", tweet.id)
        }
      }

      return query.findAll().map {
        it.asObservable<>()
      }
    }

  }
  */

  /**
   * Retrieve some Likes as [LongSparseArray]
   * Its key means tweet-id and its value means [Tag] list.
   *
   * The result is ordered by tweet-id descending.
   */
  open fun getBy(tweetList: List<Tweet>): LongSparseArray<List<Tag>> {
    Realm.getDefaultInstance().use {
      val size = tweetList.size - 1
      val query = it.where(RealmLikedTweet::class.java)
      tweetList.forEachIndexed { index, tweet ->
        if (index < size) {
          query.equalTo("tweetId", tweet.id)
              .or()
        } else {
          query.equalTo("tweetId", tweet.id)
        }
      }

      val all = query.findAllSorted("tweetId", Sort.DESCENDING)
      val table = LongSparseArray<List<Tag>>(all.size)
      all.forEach {
        val tagList = it.tagList.map { tagMapper.mapFrom(it) }
        table.append(it.tweetId, tagList)
      }

      return table
    }

  }

}
