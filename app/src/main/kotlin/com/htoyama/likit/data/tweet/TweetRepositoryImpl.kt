package com.htoyama.likit.data.tweet

import com.htoyama.likit.data.common.net.FavoriteService
import com.htoyama.likit.domain.tweet.Tweet
import com.htoyama.likit.data.tweet.TweetMapper
import com.htoyama.likit.domain.tweet.TweetRepository
import rx.Observable

/**
 * A implementation of [TweetRepository]
 */
class TweetRepositoryImpl(
    private val favoriteService: FavoriteService,
    private val mapper: TweetMapper
) : TweetRepository {

  override fun findLikedTweetList(page: Int, count: Int): Observable<List<Tweet>> {
    return favoriteService.list(null, null, count, null, null, true, page)
        .map { mapper.createListFrom(it) }
  }

}