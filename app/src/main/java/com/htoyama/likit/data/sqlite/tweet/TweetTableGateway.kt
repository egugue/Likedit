package com.htoyama.likit.data.sqlite.tweet

import com.htoyama.likit.common.AllOpen
import com.htoyama.likit.common.Contract
import com.htoyama.likit.data.sqlite.lib.SqliteOpenHelper
import com.htoyama.likit.data.sqlite.lib.SqliteScripts
import com.htoyama.likit.data.sqlite.lib.transaction
import javax.inject.Inject

/**
 * A gateway that handles data via mainly tweet table.
 */
@AllOpen
class TweetTableGateway @Inject constructor(
    private val h: SqliteOpenHelper
) {

  /**
   * Select all liked tweets as [List].
   */
  fun selectAllTweets(): List<FullTweetEntity> =
      h.readableDatabase.use {
        SqliteScripts.selectAllTweets(it)
      }

  /**
   * Select some liked tweets as [List].
   * The list is ordered by a time when the tweet was registered as like.
   *
   * @param page the number of page
   * @param perPage the number of tweets to retrieve per a page
   */
  fun selectTweet(page: Int, perPage: Int): List<FullTweetEntity> {
    Contract.require(page > 0, "0 < page required but it was $page")
    Contract.require(perPage in 1..200, "0 < perPage < 201 required but it was $perPage")

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
          SqliteScripts.insertOrUpdateIntoUser(db, ft.user)
          SqliteScripts.insertOrUpdateIntoTweet(db, ft.tweet)
        }
      }
    }
  }

  /**
   * Delete the tweet with the given id.
   */
  fun deleteTweetById(tweetId: Long) {
    h.writableDatabase.use {
      it.transaction {
        SqliteScripts.deleteTweetById(it, tweetId)
      }
    }
  }
}