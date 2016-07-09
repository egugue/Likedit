package com.htoyama.likit.data.tweet.cache

import com.htoyama.likit.data.tweet.cache.model.*
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
  @Inject internal constructor(private val mapper: Mapper) {

  fun store(likedList: List<Tweet>) {
    Realm.getDefaultInstance().use {
      it.executeTransaction {
        val realmLikedList = ArrayList<RealmTweet>(likedList.size)
        for (tweet in likedList) {
          realmLikedList.add(
              mapper.mapFrom(tweet))
        }
        it.copyToRealmOrUpdate(realmLikedList)
      }
    }
  }

  fun getList(page: Int, count: Int): Observable<List<Tweet>> {
    Realm.getDefaultInstance().use {
      val allResults = it.where(RealmTweet::class.java)
          .findAllSorted("id", Sort.DESCENDING)

      if (allResults.isEmpty()) {
        return Observable.fromCallable {
          Collections.unmodifiableList(ArrayList<Tweet>())
        }
      }

      val fromIndex = (page - 1) * count
      val toIndex = fromIndex + count
      val results: List<RealmTweet>
      if (toIndex > allResults.size) {
        results = allResults.subList(fromIndex, allResults.size)
      } else {
        results = allResults.subList(fromIndex, fromIndex + count)
      }

      val favoriteList = ArrayList<Tweet>(results.size)
      for (realmTweet in results) {
        favoriteList.add(
            mapper.mapFrom(realmTweet))
      }

      return Observable.fromCallable {
        Collections.unmodifiableList(favoriteList)
      }
    }
  }

}