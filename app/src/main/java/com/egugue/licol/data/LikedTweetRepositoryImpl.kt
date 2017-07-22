package com.egugue.licol.data

import com.egugue.licol.common.Contract
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetSqliteDao
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.likedtweet.LikedTweetRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * An implementation of [LikedTweetRepository]
 */
class LikedTweetRepositoryImpl @Inject constructor(
    private val likedTweetSqliteDao: LikedTweetSqliteDao
) : LikedTweetRepository {
  override fun find(page: Int, perPage: Int): Observable<List<LikedTweet>> {
    Contract.require(page > 0, "0 < page required but it was $page")
    Contract.require(perPage in 1..200, "1 <= perPage <= 200 required but it was $perPage")

    return likedTweetSqliteDao.select(page, perPage)
  }

  override fun findByTagId(tagId: Long, page: Int, perPage: Int): Observable<List<LikedTweet>> {
    Contract.require(tagId >= 0, "tagId >= 0 required but it was $tagId")
    Contract.require(page > 0, "0 < page required but it was $page")
    Contract.require(perPage in 1..200, "1 <= perPage <= 200 required but it was $perPage")

    return likedTweetSqliteDao.selectByTagId(tagId, page, perPage)
  }

  override fun findByUserId(userId: Long, page: Int, perPage: Int): Observable<List<LikedTweet>> {
    Contract.require(userId >= 0, "userId >= 0 required but it was $userId")
    Contract.require(page > 0, "0 < page required but it was $page")
    Contract.require(perPage in 1..200, "1 <= perPage <= 200 required but it was $perPage")

    return likedTweetSqliteDao.selectByUserId(userId, page, perPage)
  }

  override fun findByTextContaining(partOfText: String, page: Int,
      perPage: Int): Observable<List<LikedTweet>> {
    return likedTweetSqliteDao.selectByTextContaining(partOfText, page, perPage)
  }

}