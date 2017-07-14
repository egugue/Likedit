package com.egugue.licol.data.sqlite.relation

import android.annotation.SuppressLint
import com.egugue.licol.common.AllOpen
import com.egugue.licol.common.Contract
import com.egugue.licol.data.sqlite.lib.SqliteScripts
import com.egugue.licol.data.sqlite.lib.createQuery
import com.egugue.licol.data.sqlite.lib.transaction
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A gateway that handles data via mainly tweet_tag_relation table.
 *
 * NOTE: Tweet of this class name means "Liked Tweet".
 */
@AllOpen
@SuppressLint("CheckResult")
class TweetTagRelationTableGateway @Inject constructor(
    private val db: BriteDatabase
) {

  /**
   * Select some relations related to the given tweet id list.
   */
  fun selectRelationsByTweetIdList(tweetIdList: List<Long>): Observable<List<TweetTagRelation>> {
    Contract.require(tweetIdList.isNotEmpty(), "the given list must not be empty")

    val stmt = TweetTagRelation.FACTORY.select_by_tweet_id_list(tweetIdList.toLongArray())

    return db.createQuery(stmt)
        .mapToList { TweetTagRelation.FACTORY.select_by_tweet_id_listMapper().map(it) }
  }

  /**
   * Select some relations related to the given tag id list
   */
  fun selectRelationsByTagIdList(tagIdList: List<Long>): Observable<List<TweetTagRelation>> {
    Contract.require(tagIdList.isNotEmpty(), "the given list must not be empty")

    val stmt = TweetTagRelation.FACTORY.select_by_tag_id_list(tagIdList.toLongArray())

    return db.createQuery(stmt)
        .mapToList { TweetTagRelation.FACTORY.select_by_tag_id_listMapper().map(it) }
  }

  /**
   * Select all relations between a liked tweet and a tag.
   */
  fun selectAllTweetTagRelations(): Observable<List<TweetTagRelation>> {
    val stmt = TweetTagRelation.FACTORY.select_all()

    return db.createQuery(stmt)
        .mapToList { TweetTagRelation.FACTORY.select_allMapper().map(it) }
  }

  /**
   * Insert relations between a liked tweet and a tag.
   */
  fun insertTweetTagRelation(list: List<TweetTagRelation>) {
    db.transaction {
      list.forEach { (tweetId, tagId) ->
        SqliteScripts.insertTweetTagRelation(db, tweetId, tagId)
      }
    }
  }

  /**
   * Delete relations between a liked tweet and a tag.
   */
  fun deleteTweetTagRelation(list: List<TweetTagRelation>) {
    db.transaction {
      list.forEach { (tweetId, tagId) ->
        SqliteScripts.deleteTweetTagRelation(db, tweetId, tagId)
      }
    }
  }

  fun deleteByTweetIdList(list: List<Long>) {
    val stmt = TweetTagRelation.FACTORY.delete_by_tag_id_list(list.toLongArray())
    db.executeUpdateDelete(stmt.tables, db.writableDatabase.compileStatement(stmt.statement))
  }
}
