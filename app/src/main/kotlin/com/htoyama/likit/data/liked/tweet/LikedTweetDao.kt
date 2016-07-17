package com.htoyama.likit.data.liked.tweet

import com.htoyama.likit.data.common.net.FavoriteService
import com.htoyama.likit.data.liked.LikedRealmGateway
import com.htoyama.likit.data.liked.tweet.cache.LikedTweetCacheGateway
import com.htoyama.likit.data.tweet.TweetMapper
import com.htoyama.likit.domain.tweet.Tweet
import rx.Observable
import javax.inject.Inject

/**
 * A Data Access Object that handles liked Tweet by authenticated user.
 */
class LikedTweetDao
    @Inject internal constructor(
        private val favoriteService: FavoriteService,
        private val cacheGateway: LikedTweetCacheGateway,
        private val likedRealmGateway: LikedRealmGateway,
        private val tweetMapper: TweetMapper) {

  /**
   * Retrieve liked [Tweet]s List by current authenticated user.
   */
  fun getTweetList(page: Int, count: Int): Observable<List<Tweet>> {
     assert(count > 0)
     assert(page > 0)

     val fromCache = cacheGateway.getList(page, count)
     val fromNet: Observable<List<Tweet>> = favoriteService.list(null, null, count, null, null, true, page)
         .map {
             it.map { tweetMapper.createFrom(it) }
         }
         .doOnNext { tweetList ->
             cacheGateway.store(tweetList)
             likedRealmGateway.insertAsContainingNoTag(tweetList)
         }

     //TODO: https://github.com/egugue/Likedit/issues/24
     return Observable.concat(fromCache, fromNet)
             .first { cached -> cached.isNotEmpty() && cached.size >= count}
    }

}
