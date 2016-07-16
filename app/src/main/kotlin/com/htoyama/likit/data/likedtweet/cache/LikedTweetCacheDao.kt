package com.htoyama.likit.data.likedtweet.cache

import com.htoyama.likit.data.likedtweet.cache.model.*
import com.htoyama.likit.domain.likedtweet.LikedTweet
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tweet.Tweet
import io.realm.Realm
import io.realm.Sort
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * Created by toyamaosamuyu on 2016/07/03.
 */
class LikedTweetCacheDao
  @Inject internal constructor(
      private val tweetMapper: TweetMapper,
      private val mapper: LikedTweetMapper) {

  fun store(tweetList: List<Tweet>) {
    Realm.getDefaultInstance().use {
      it.executeTransaction {
        val realmTweetList = tweetList.map {
          tweetMapper.mapFrom(it) }
        it.insertOrUpdate(realmTweetList)
      }
    }

  }

  /*
  fun store(likedList: List<LikedTweet>) {
    Realm.getDefaultInstance().use {
      it.executeTransaction {
        val realmLikedList = likedList.map {
          mapper.mapFrom(it) }
        it.copyToRealmOrUpdate(realmLikedList)
      }
    }
  }
  */

  fun getList(page: Int, count: Int): Observable<List<Tweet>> {
    return Observable.fromCallable {
      Realm.getDefaultInstance().use { realm ->
        val allResults = realm.where(RealmTweet::class.java)
            .findAllSorted("id", Sort.DESCENDING)
        if (allResults.isEmpty()) {
          Collections.unmodifiableList(ArrayList<LikedTweet>())
        }

        val fromIndex = (page - 1) * count
        val toIndex = fromIndex + count
        val results: List<RealmTweet>
        if (toIndex > allResults.size) {
          results = allResults.subList(fromIndex, allResults.size)
        } else {
          results = allResults.subList(fromIndex, fromIndex + count)
        }

        val favoriteList = results.map {
          tweetMapper.mapFrom(it)
        }

        Collections.unmodifiableList(favoriteList)
      }
    }
  }

  /*
  fun getListByTag(tag: Tag): Observable<List<LikedTweet>> {
    Realm.getDefaultInstance().use { realm ->
      return Observable.fromCallable {
        val allResults = realm.where(RealmLikedTweet::class.java)
            .equalTo("tag.id", tag.id)
            .findAllSorted("tweet.id", Sort.DESCENDING)

        val likedList = allResults.map {
          mapper.mapFrom(it)
        }

        Collections.unmodifiableList(likedList)
      }
    }
  }
  */

}