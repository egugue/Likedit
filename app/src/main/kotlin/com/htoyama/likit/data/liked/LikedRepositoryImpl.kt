package com.htoyama.likit.data.liked

import com.htoyama.likit.data.liked.tweet.LikedTweetDao
import com.htoyama.likit.domain.liked.LikedTweet
import com.htoyama.likit.domain.liked.LikedRepository
import com.htoyama.likit.domain.tag.Tag
import rx.Observable

/**
 * Created by toyamaosamuyu on 2016/07/16.
 */
class LikedRepositoryImpl constructor(
    private val likedTweetDao: LikedTweetDao,
    private val likedRealmGateway: LikedRealmGateway,
    private val likedFactory: LikedFactory) : LikedRepository {

  override fun find(page: Int, count: Int): Observable<List<LikedTweet>> {
    return likedTweetDao.getTweetList(page, count)
        .map {
          val realmLikedList = likedRealmGateway.getBy(it)
          likedFactory.createFrom(realmLikedList, it)
        }
  }

  override fun findByTag(tag: Tag): Observable<List<Tag>> {
    throw UnsupportedOperationException()
  }

}
