package com.htoyama.likit.data.likedtweet.cache

import com.htoyama.likit.data.likedtweet.cache.model.*
import com.htoyama.likit.data.likedtweet.tweet.cache.TweetMapper
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