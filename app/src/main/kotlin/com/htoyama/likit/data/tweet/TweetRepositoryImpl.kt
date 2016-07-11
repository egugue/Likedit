package com.htoyama.likit.data.tweet

import android.util.Log
import com.htoyama.likit.data.common.net.FavoriteService
import com.htoyama.likit.data.tweet.TweetMapper
import com.htoyama.likit.data.likedtweet.cache.LikedTweetCacheDao
import com.htoyama.likit.domain.tweet.Tweet
import com.htoyama.likit.domain.tweet.TweetRepository
import rx.Observable

/**
 * A implementation of [TweetRepository]
 */
class TweetRepositoryImpl(
    private val favoriteService: FavoriteService,
    private val mapper: TweetMapper,
    private val likedCacheDao: LikedTweetCacheDao
) : TweetRepository {

  override fun findLikedTweetList(page: Int, count: Int): Observable<List<Tweet>> {
    val fromCache = likedCacheDao.getList(page, count)

    val fromNetwork = favoriteService.list(null, null, count, null, null, true, page)
        .map { mapper.createListFrom(it) }
        .doOnNext { likedCacheDao.store(it) }

    return Observable.concat(fromCache, fromNetwork)
        .first { cached ->
          Log.d("ーーー", "chache isNotEmpty: " + cached.isNotEmpty().toString())
          cached.isNotEmpty() }
  }

}