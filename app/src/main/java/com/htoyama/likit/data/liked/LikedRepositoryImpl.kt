package com.htoyama.likit.data.liked

import com.htoyama.likit.data.liked.tweet.LikedTweetDao
import com.htoyama.likit.domain.liked.LikedTweet
import com.htoyama.likit.domain.liked.LikedRepository
import com.htoyama.likit.domain.tag.Tag
import rx.Observable

/**
 * The implementation of [LikedRepository]
 */
class LikedRepositoryImpl constructor(
    private val likedTweetDao: LikedTweetDao,
    private val likedRealmGateway: LikedRealmGateway
) : LikedRepository {

  override fun find(page: Int, count: Int): Observable<List<LikedTweet>> {
    return likedTweetDao.getTweetList(page, count)
        .map { tweetList -> likedRealmGateway.getBy(tweetList) }
  }

  override fun findByTag(tag: Tag): Observable<List<Tag>> {
    throw UnsupportedOperationException()
  }

}
