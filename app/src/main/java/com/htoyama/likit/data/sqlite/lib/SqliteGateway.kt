package com.htoyama.likit.data.sqlite.lib

import com.htoyama.likit.common.Contract
import com.htoyama.likit.data.sqlite.entity.FullTweetEntity
import com.htoyama.likit.data.sqlite.entity.TagEntity
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
          SqliteScripts.insertOrUpdateIntoTweet(db, ft.tweet)
          SqliteScripts.insertOrUpdateIntoUser(db, ft.user)
        }
      }
    }
  }

  /**
   * Insert the given name and created as a Tag.
   *
   * @return the row ID of the last row inserted, if this insert is successful. -i otherwise.
   */
  fun insertTag(name: String, created: Long): Long =
      h.writableDatabase.use {
        it.transaction {
          SqliteScripts.insertTag(it, name, created)
        }
      }

  /**
   * Select the tag which has the given id.
   * If there is no such tag, return null.
   */
  fun selectTagById(id: Long): TagEntity? {
    return h.readableDatabase.use {
      SqliteScripts.selectTagById(it, id)
    }
  }

  /**
   * Search tags which name contains the given name.
   */
  fun searchTagByName(name: String): List<TagEntity> {
    val escaped = name.replace("%", "$%")
        .replace("_", "\$_")
    return h.readableDatabase.use {
      SqliteScripts.searchTagByName(it, escaped)
    }
  }
}