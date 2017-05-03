package com.htoyama.likit.data.sqlite.relation

import com.htoyama.likit.common.AllOpen
import com.htoyama.likit.common.Contract
import com.htoyama.likit.common.extensions.toV2Observable
import com.htoyama.likit.data.sqlite.lib.SqliteScripts
import com.htoyama.likit.data.sqlite.lib.createQuery
import com.htoyama.likit.data.sqlite.lib.transaction
import com.squareup.sqlbrite.BriteDatabase
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A gateway that handles data via mainly tweet_tag_relation table.
 *
 * NOTE: Tweet of this class name means "Liked Tweet".
 */
@AllOpen
class TweetTagRelationTableGateway @Inject constructor(
    private val db: BriteDatabase
) {

  /**
   * Select some relations related to the given tweet id list.
   */
  fun selectRelationsByTweetIdList(tweetIdList: List<Long>): Observable<List<TweetTagRelation>> {
    Contract.require(tweetIdList.isNotEmpty(), "the given list must not empty")

    val stmt = TweetTagRelation.FACTORY.select_by_tweet_id_list(tweetIdList.toLongArray())

    return db.createQuery(stmt)
        .mapToList { TweetTagRelation.FACTORY.select_by_tweet_id_listMapper().map(it) }
        .toV2Observable()
  }

  /**
   * Select some relations related to the given tag id list
   */
  fun selectRelationsByTagIdList(tagIdList: List<Long>): Observable<List<TweetTagRelation>> {
    Contract.require(tagIdList.isNotEmpty(), "the given list must not empty")

    val stmt = TweetTagRelation.FACTORY.select_by_tag_id_list(tagIdList.toLongArray())

    return db.createQuery(stmt)
        .mapToList { TweetTagRelation.FACTORY.select_by_tag_id_listMapper().map(it) }
        .toV2Observable()
  }

  /**
   * Select all relations between a liked tweet and a tag.
   */
  fun selectAllTweetTagRelations(): Observable<List<TweetTagRelation>> {
    val stmt = TweetTagRelation.FACTORY.select_all()

    return db.createQuery(stmt)
        .mapToList { TweetTagRelation.FACTORY.select_allMapper().map(it) }
        .toV2Observable()
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
}
