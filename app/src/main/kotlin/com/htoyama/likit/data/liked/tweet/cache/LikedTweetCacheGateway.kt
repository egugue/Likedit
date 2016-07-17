package com.htoyama.likit.data.liked.tweet.cache

import com.htoyama.likit.domain.tweet.Tweet
import io.realm.Realm
import io.realm.Sort
import java.util.*
import javax.inject.Inject
import rx.Observable

/**
 * Created by toyamaosamuyu on 2016/07/16.
 */
class LikedTweetCacheGateway
  @Inject internal constructor(private val mapper: Mapper) {

  fun store(tweetList: List<Tweet>) {
    Realm.getDefaultInstance().use {
      it.executeTransaction {
        val realmTweetList = tweetList.map {
          mapper.mapFrom(it) }
        it.insertOrUpdate(realmTweetList)
      }
    }
  }

  fun getList(page: Int, count: Int): Observable<List<Tweet>> {
    return Observable.fromCallable {
      Realm.getDefaultInstance().use { realm ->
        val allResults = realm.where(RealmTweet::class.java)
            .findAllSorted("id", Sort.DESCENDING)
        if (allResults.isEmpty()) {
          Collections.unmodifiableList(ArrayList<Tweet>())
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
          mapper.mapFrom(it)
        }

        Collections.unmodifiableList(favoriteList)
      }
    }
  }

}
