package com.htoyama.likit.data.sqlite.relation

import com.htoyama.likit.common.extensions.toV2Observable
import com.htoyama.likit.data.sqlite.lib.SqliteScripts
import com.htoyama.likit.data.sqlite.lib.createQuery
import com.htoyama.likit.data.sqlite.lib.transaction
import com.squareup.sqlbrite.BriteDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A gateway that handles data via mainly tweet_tag_relation table.
 *
 * NOTE: Tweet of this class name means "Liked Tweet".
 */
class TweetTagRelationTableGateway @Inject constructor(
    private val db: BriteDatabase
) {

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
  fun insertTweetTagRelation(tweetIdList: List<Long>, tagId: Long): Completable {
    return Completable.fromAction {
      db.writableDatabase.use { db ->
        db.transaction {
          tweetIdList.forEach { tweetId ->
            SqliteScripts.insertTweetTagRelation(db, tweetId, tagId)
          }
        }
      }
    }
  }

  /**
   * Delete relations between a liked tweet and a tag.
   */
  fun deleteTweetTagRelation(tweetIdList: List<Long>, tagId: Long): Completable {
    return Completable.fromAction {
      db.writableDatabase.use { db ->
        db.transaction {
          tweetIdList.forEach { tweetId ->
            SqliteScripts.deleteTweetTagRelation(db, tweetId, tagId)
          }
        }
      }
    }
  }
}
