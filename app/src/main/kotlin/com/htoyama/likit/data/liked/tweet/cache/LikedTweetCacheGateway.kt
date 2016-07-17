package com.htoyama.likit.data.liked.tweet.cache

import com.htoyama.likit.domain.tweet.Tweet
import io.realm.Realm
import io.realm.Sort
import java.util.*
import javax.inject.Inject
import rx.Observable

/**
 * A gateway that handles cached liked-tweet data via cache repository.
 */
class LikedTweetCacheGateway
  @Inject internal constructor(private val mapper: Mapper) {

  /**
   * Store the given tweets list.
   *
   * Note: If the Cache already havs a tweet in the given list,
   * the cache updates the tweet data instead of ignoring.
   */
  fun store(tweetList: List<Tweet>) {
    Realm.getDefaultInstance().use {
      it.executeTransaction {
        val realmTweetList = tweetList.map {
          mapper.mapFrom(it) }
        it.insertOrUpdate(realmTweetList)
      }
    }
  }

  /**
   * Retrive liked-tweet list as [Observable]
   */
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
