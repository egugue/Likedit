package com.htoyama.likit.data

import com.htoyama.likit.common.Contract
import com.htoyama.likit.data.sqlite.likedtweet.LikedTweetSqliteDao
import com.htoyama.likit.domain.likedtweet.LikedTweet
import com.htoyama.likit.domain.likedtweet.LikedTweetRepository
import io.reactivex.Observable
import javax.inject.Inject

class LikedTweetRepositoryImpl @Inject constructor(
    private val likedTweetSqliteDao: LikedTweetSqliteDao
): LikedTweetRepository {

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
}