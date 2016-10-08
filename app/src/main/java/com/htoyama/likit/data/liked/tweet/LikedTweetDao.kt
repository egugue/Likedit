package com.htoyama.likit.data.liked.tweet

import com.htoyama.likit.common.Contract
import com.htoyama.likit.data.common.net.FavoriteService
import com.htoyama.likit.data.liked.LikedRealmGateway
import com.htoyama.likit.data.liked.tweet.cache.LikedTweetCacheGateway
import com.htoyama.likit.data.tweet.TweetMapper
import com.htoyama.likit.domain.tweet.Tweet
import io.reactivex.Single
import javax.inject.Inject

/**
 * A Data Access Object that handles liked Tweet by authenticated user.
 */
open class LikedTweetDao @Inject internal constructor(
    private val favoriteService: FavoriteService,
    private val cacheGateway: LikedTweetCacheGateway,
    private val likedRealmGateway: LikedRealmGateway,
    private val tweetMapper: TweetMapper
) {

  /**
   * Retrieve liked [Tweet]s List by current authenticated user.
   */
  open fun getTweetList(page: Int, count: Int): Single<List<Tweet>> {
    Contract.require(page > 0, "page must be greater than or equal to 0")
    Contract.require(count > 0, "count must be greater than or equal to 0")

    val fromCache = cacheGateway.getList(page, count)

    val fromNet: Single<List<Tweet>> = favoriteService.list(null, null, count, null, null, true, page)
        .map { it.map { tweetMapper.createFrom(it) } }
        .doOnSuccess { tweetList -> likedRealmGateway.insertAsContainingNoTag(tweetList) }

    //TODO: https://github.com/egugue/Likedit/issues/24
    return Single.concat(fromCache, fromNet)
        .filter { cached -> cached.isNotEmpty() && cached.size >= count }
        .firstElement()
        .toSingle()
  }

}
