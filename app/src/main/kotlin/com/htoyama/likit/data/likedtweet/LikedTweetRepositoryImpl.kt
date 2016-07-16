package com.htoyama.likit.data.likedtweet

import com.htoyama.likit.data.likedtweet.tweet.LikedTweetDao
import com.htoyama.likit.domain.likedtweet.LikedTweet
import com.htoyama.likit.domain.likedtweet.LikedTweetRepository
import com.htoyama.likit.domain.tag.Tag
import rx.Observable

/**
 * Created by toyamaosamuyu on 2016/07/16.
 */
class LikedTweetRepositoryImpl constructor(
    private val likedTweetDao: LikedTweetDao,
    private val likedRealmGateway: LikedRealmGateway,
    private val likedFactory: RealmTweetFactory) : LikedTweetRepository {

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
