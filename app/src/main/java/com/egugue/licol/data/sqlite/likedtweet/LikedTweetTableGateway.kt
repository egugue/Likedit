package com.egugue.licol.data.sqlite.likedtweet

import android.annotation.SuppressLint
import com.egugue.licol.common.AllOpen
import com.egugue.licol.common.Contract
import com.egugue.licol.data.sqlite.lib.SqliteScripts
import com.egugue.licol.data.sqlite.lib.createQuery
import com.egugue.licol.data.sqlite.lib.escapeForQuery
import com.egugue.licol.data.sqlite.lib.toLimitAndOffset
import com.egugue.licol.data.sqlite.lib.transaction
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A gateway that handles data via mainly tweet table.
 */
@AllOpen
@SuppressLint("CheckResult")
class LikedTweetTableGateway @Inject constructor(
    private val db: BriteDatabase
) {

  /**
   * Select all liked tweets as [List].
   */
  fun selectAllTweets(): Observable<List<FullLikedTweetEntity>> {
    val stmt = LikedTweetEntity.FACTORY.select_all()

    return db.createQuery(stmt)
        .mapToList { FullLikedTweetEntity.MAPPER.map(it) }
  }

  fun selectIdByUserIds(userIdList: List<Long>): Observable<List<LikedTweetIdAndUserId>> {
    val stmt = LikedTweetEntity.FACTORY.select_id_by_user_ids(userIdList.toLongArray())
    return db.createQuery(stmt)
        .mapToList { LikedTweetIdAndUserId.MAPPER.map(it) }
  }

  /**
   * Select some liked tweets as [List].
   * The list is ordered by a time when the tweet was registered as like.
   *
   * @param page the number of page
   * @param perPage the number of tweets to retrieve per a page
   */
  fun selectTweet(page: Int, perPage: Int): Observable<List<FullLikedTweetEntity>> {
    Contract.require(page > 0, "0 < page required but it was $page")
    Contract.require(perPage in 1..200, "0 < perPage < 201 required but it was $perPage")

    val (limit, offset) = (page to perPage).toLimitAndOffset()
    val stmt = LikedTweetEntity.FACTORY.select_liked_tweets(limit, offset)

    return db.createQuery(stmt)
        .mapToList { FullLikedTweetEntity.MAPPER.map(it) }
  }

  /**
   * Select some liked tweets as [List] by the given tweet ids.
   * The list is ordered by a time when the tweet was registered as like.
   *
   * @param tweetIdList the list of tweet id
   * @param page the number of page
   * @param perPage the number of tweets to retrieve per a page
   */
  fun selectByTweetIdList(tweetIdList: List<Long>, page: Int, perPage: Int): Observable<List<FullLikedTweetEntity>> {
    val (limit, offset) = (page to perPage).toLimitAndOffset()
    val stmt = LikedTweetEntity.FACTORY.select_liked_tweets_by_ids(
        tweetIdList.toLongArray(), limit, offset)

    return db.createQuery(stmt)
        .mapToList { FullLikedTweetEntity.MAPPER.map(it) }
  }

  fun selectByUserId(userId: Long, page: Int, perPage: Int): Observable<List<FullLikedTweetEntity>> {
    val (limit, offset) = (page to perPage).toLimitAndOffset()
    val stmt = LikedTweetEntity.FACTORY.select_liked_tweets_by_user_id(userId, limit, offset)

    return db.createQuery(stmt)
        .mapToList { FullLikedTweetEntity.MAPPER.map(it) }
  }

  fun searchByTextContaining(partOfText: String, page: Int, perPage: Int): Observable<List<FullLikedTweetEntity>> {
    val (limit, offset) = (page to perPage).toLimitAndOffset()
    val escaped = partOfText.escapeForQuery()
    val stmt = LikedTweetEntity.FACTORY.search_liked_tweets_by_text(escaped, limit, offset)

    return db.createQuery(stmt)
        .mapToList { FullLikedTweetEntity.MAPPER.map(it) }
  }

  /**
   * Insert or ignore the given tweet.
   */
  fun insertOrIgnoreTweet(fullTweet: FullLikedTweetEntity) {
    insertOrIgnoreTweetList(listOf(fullTweet))
  }

  /**
   * Insert or ignore the given tweet.
   */
  fun insertOrIgnoreTweetList(fullTweetList: List<FullLikedTweetEntity>) {
    Contract.require(fullTweetList.isNotEmpty(), "fullTweetList must not be emtpy. but it's size was " + fullTweetList.size)

    db.transaction {
      fullTweetList.forEach { (tweet, quoted) ->
        if (quoted != null) {
          SqliteScripts.insertOrIgnoreQuotedTweet(db, quoted)
        }
        SqliteScripts.insertOrIgnoreIntoLikedTweet(db, tweet)
      }
    }
  }

  /**
   * Delete the tweet with the given id.
   */
  fun deleteTweetById(tweetId: Long) {
    deleteTweetByIdList(listOf(tweetId))
  }

  fun deleteTweetByIdList(tweetIdList: List<Long>) {
    db.transaction {
      tweetIdList.forEach {
        SqliteScripts.deleteLikedTweetById(db, it)
      }
    }
  }
}