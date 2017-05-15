package com.egugue.licol.data.sqlite.likedtweet

import com.egugue.licol.common.AllOpen
import com.egugue.licol.common.Contract
import com.egugue.licol.common.extensions.toV2Observable
import com.egugue.licol.data.sqlite.lib.SqliteScripts
import com.egugue.licol.data.sqlite.lib.createQuery
import com.egugue.licol.data.sqlite.lib.toLimitAndOffset
import com.egugue.licol.data.sqlite.lib.transaction
import com.squareup.sqlbrite.BriteDatabase
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A gateway that handles data via mainly tweet table.
 */
@AllOpen
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
        .toV2Observable()
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
        .toV2Observable()
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
        .toV2Observable()
  }

  /**
   * Insert or update the given tweet.
   */
  fun insertOrUpdateTweet(fullTweet: FullLikedTweetEntity) {
    insertOrUpdateTweetList(listOf(fullTweet))
  }

  /**
   * Insert or update the given tweet.
   */
  fun insertOrUpdateTweetList(fullTweetList: List<FullLikedTweetEntity>) {
    Contract.require(fullTweetList.isNotEmpty(), "fullTweetList must not be emtpy. but it's size was " + fullTweetList.size)

    db.transaction {
      fullTweetList.forEach { (tweet, user) ->
        SqliteScripts.insertOrUpdateIntoUser(db, user)
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
      tweetIdList.map {
        SqliteScripts.deleteLikedTweetById(db, it)
      }
    }
  }
}