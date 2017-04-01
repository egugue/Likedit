package com.htoyama.likit.data.sqlite.lib

import com.htoyama.likit.common.Contract
import com.htoyama.likit.data.sqlite.entity.FullTweetEntity
import com.htoyama.likit.data.sqlite.entity.TagEntity
import com.htoyama.likit.data.sqlite.entity.TweetTagRelation
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
          SqliteScripts.insertOrUpdateIntoUser(db, ft.user)
          SqliteScripts.insertOrUpdateIntoTweet(db, ft.tweet)
        }
      }
    }
  }

  /**
   * Select the tag with the given id.
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
   * Update the name of the tag with the given id.
   *
   * @throws IllegalStateException if the tag with the id has not inserted.
   */
  fun updateTagNameById(id: Long, name: String) {
    h.writableDatabase.use {
      it.transaction {
        if (SqliteScripts.selectTagById(it, id) == null) {
          throw IllegalArgumentException("tried to update the name of the tag with id($id). but it has not inserted.")
        }

        SqliteScripts.updateTagNameById(it, name, id)
      }
    }
  }

  /**
   * Delete the tag with the given id.
   *
   * @throws IllegalStateException if there is no such tag
   */
  fun deleteTagById(id: Long) {
    h.writableDatabase.use {
      it.transaction {
        if (SqliteScripts.selectTagById(it, id) == null) {
          throw IllegalStateException("tried to delete the tag with id($id). but there is no such tag.")
        }
        SqliteScripts.deleteTagById(it, id)
      }
    }
  }

  /**
   * Select all relations between a tweet and a tag.
   */
  fun selectAllTweetTagRelations(): List<TweetTagRelation> {
    return h.readableDatabase.use {
      SqliteScripts.selectAllTweetTagRelations(it)
    }
  }

  /**
   * Insert relations between a tweet and a tag.
   */
  fun insertTweetTagRelation(tweetIdlist: List<Long>, tagId: Long) {
    h.writableDatabase.use { db ->
      db.transaction {
        tweetIdlist.forEach { tweetId ->
          SqliteScripts.insertTweetTagRelation(db, tweetId, tagId)
        }
      }
    }
  }

  /**
   * Delete relations between a tweet and a tag.
   */
  fun deleteTweetTagRelation(tweetIdlist: List<Long>, tagId: Long) {
    h.writableDatabase.use { db ->
      db.transaction {
        tweetIdlist.forEach { tweetId ->
          SqliteScripts.deleteTweetTagRelation(db, tweetId, tagId)
        }
      }
    }
  }
}