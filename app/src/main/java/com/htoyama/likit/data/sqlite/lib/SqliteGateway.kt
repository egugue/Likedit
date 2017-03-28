package com.htoyama.likit.data.sqlite.lib

import android.support.annotation.VisibleForTesting
import com.htoyama.likit.common.Contract
import com.htoyama.likit.data.sqlite.entity.FullTweetEntity
import javax.inject.Inject

/**
 * Gateway that handles data via SQLite tables.
 */
class SqliteGateway @Inject constructor(
    private val h: SqliteOpenHelper
) {

  /**
   * Select all liked tweets as [List].
   */
  fun selectAllTweets(): List<FullTweetEntity> {
    return h.readableDatabase.use {
      SqliteScripts.selectAllTweets(it)
    }
  }

  /**
   * Select some liked tweets as [List].
   * The list is ordered by a time when the tweet was registered as like.
   *
   * @param page the number of page
   * @param perPage the number of tweets to retrieve per a page
   */
  fun selectTweet(page: Int, perPage: Int): List<FullTweetEntity> {
    Contract.require(page > 0, "page > 0 but it was $page")
    Contract.require(perPage in 1..200, "0 < perpage < 201 but it was $perPage")

    return h.readableDatabase.use {
      val limit = perPage.toLong()
      val offset = (page - 1) * limit
      SqliteScripts.selectTweets(limit, offset, it)
    }
  }

  /**
   * Insert or update the given tweet.
   */
  fun insertOrUpdateTweet(fullTweet: FullTweetEntity) {
    insertOrUpdateTweetList(listOf(fullTweet))
  }

  /**
   * Insert or update the given tweet.
   */
  fun insertOrUpdateTweetList(fullTweetList: List<FullTweetEntity>) {
    Contract.require(fullTweetList.isNotEmpty(), "fullTweetList must not be emtpy. but it's size was " + fullTweetList.size)

    h.writableDatabase.use { db ->
      db.transaction {
        fullTweetList.forEach { ft ->
          SqliteScripts.insertOrUpdateIntoTweet(db, ft.tweet)
          SqliteScripts.insertOrUpdateIntoUser(db, ft.user)
        }
      }
    }
  }
}