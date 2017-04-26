package com.htoyama.likit.data.liked

import com.htoyama.likit.data.liked.tweet.LikedTweetDao
import com.htoyama.likit.domain.likedtweet.LikedTweet
import com.htoyama.likit.domain.likedtweet.LikedRepository
import com.htoyama.likit.domain.tag.Tag
import io.reactivex.Single

/**
 * The implementation of [LikedRepository]
 */
class LikedRepositoryImpl constructor(
    private val likedTweetDao: LikedTweetDao,
    private val likedRealmGateway: LikedRealmGateway
) : LikedRepository {

  override fun find(page: Int, count: Int): Single<List<LikedTweet>> {
    return likedTweetDao.getTweetList(page, count)
        .map { tweetList -> likedRealmGateway.getBy(tweetList) }
  }

  override fun findByTag(tag: Tag): Single<List<Tag>> {
    throw UnsupportedOperationException()
  }

}
