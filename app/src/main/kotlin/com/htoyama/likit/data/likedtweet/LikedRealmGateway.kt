package com.htoyama.likit.data.likedtweet

import android.util.LongSparseArray
import com.htoyama.likit.data.likedtweet.RealmLikedTweet
import com.htoyama.likit.data.tag.TagMapper
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tweet.Tweet
import io.realm.Realm
import io.realm.RealmList
import io.realm.Sort
import javax.inject.Inject

/**
 * Created by toyamaosamuyu on 2016/07/16.
 */
class LikedRealmGateway @Inject internal constructor(private val tagMapper: TagMapper) {

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

  fun getBy(tweetList: List<Tweet>): LongSparseArray<List<Tag>> {
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
