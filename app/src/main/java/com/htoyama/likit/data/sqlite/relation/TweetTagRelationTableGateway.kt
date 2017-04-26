package com.htoyama.likit.data.sqlite.relation

import com.htoyama.likit.data.sqlite.lib.SqliteOpenHelper
import com.htoyama.likit.data.sqlite.lib.SqliteScripts
import com.htoyama.likit.data.sqlite.lib.transaction
import javax.inject.Inject

/**
 * A gateway that handles data via mainly tweet_tag_relation table.
 *
 * NOTE: Tweet of this class name means "Liked Tweet".
 */
class TweetTagRelationTableGateway @Inject constructor(
    private val h: SqliteOpenHelper
) {

  /**
   * Select all relations between a liked tweet and a tag.
   */
  fun selectAllTweetTagRelations(): List<TweetTagRelation> {
    return h.readableDatabase.use {
      SqliteScripts.selectAllTweetTagRelations(it)
    }
  }

  /**
   * Insert relations between a liked tweet and a tag.
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
   * Delete relations between a liked tweet and a tag.
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
