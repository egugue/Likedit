package com.htoyama.likit.data.sqlite.lib

import android.database.sqlite.SQLiteDatabase
import com.htoyama.likit.data.sqlite.TagModel
import com.htoyama.likit.data.sqlite.TweetModel
import com.htoyama.likit.data.sqlite.TweetTagRelationModel
import com.htoyama.likit.data.sqlite.UserModel
import com.htoyama.likit.data.sqlite.relation.TweetTagRelation
import com.htoyama.likit.data.sqlite.tag.TagEntity
import com.htoyama.likit.data.sqlite.tweet.FullTweetEntity
import com.htoyama.likit.data.sqlite.tweet.TweetEntity
import com.htoyama.likit.data.sqlite.user.UserEntity

/**
 * A collection which has a lot of simple SQLite scripts.
 *
 * NOTE:
 * Each script is expected to be used in combination with other class
 * so that they should not close a given [SQLiteDatabase].
 */
internal object SqliteScripts {

  /**
   * Select all stored liked tweets as list.
   */
  fun selectAllTweets(readable: SQLiteDatabase): List<FullTweetEntity> {
    val stmt = TweetEntity.FACTORY.select_all()
    return readable.rawQuery(stmt.statement, stmt.args)
        .mapToList { FullTweetEntity.MAPPER.map(it) }
  }

  fun selectTweets(limit: Long, offset: Long, readable: SQLiteDatabase): List<FullTweetEntity> {
    val stmt = TweetEntity.FACTORY.select_tweets(limit, offset)
    return readable.rawQuery(stmt.statement, stmt.args)
        .mapToList { FullTweetEntity.MAPPER.map(it) }
  }

  fun insertOrIgnoreIntoTweet(writable: SQLiteDatabase, tweet: TweetEntity) {
    val stmt = TweetModel.Insert_tweet(writable, TweetEntity.FACTORY)
    tweet.apply {
      stmt.bind(id, userId, text, imageList, urlList, video, created)
    }
    stmt.program.executeInsert()
  }

  fun deleteTweetById(writable: SQLiteDatabase, tweetId: Long) =
      TweetModel.Delete_by_id(writable).run {
        bind(tweetId)
        program.executeUpdateDelete()
      }

  fun insertOrUpdateIntoUser(writable: SQLiteDatabase, user: UserEntity) {
    val stmt = UserModel.Insert_user(writable)
    user.apply {
      stmt.bind(id, name, screenName, avatarUrl)
    }
    stmt.program.executeInsert()
  }

  fun insertTag(writable: SQLiteDatabase, name: String, created: Long): Long =
      TagModel.Insert_(writable).run {
        bind(name, created)
        program.executeInsert()
      }

  fun updateTagNameById(writable: SQLiteDatabase, name: String, id: Long) =
      TagModel.Update_name(writable).run {
        bind(name, id)
        program.executeUpdateDelete()
      }

  fun deleteTagById(writable: SQLiteDatabase, id: Long) =
      TagModel.Delete_by_id(writable).run {
        bind(id)
        program.executeUpdateDelete()
      }

  fun selectTagById(readable: SQLiteDatabase, id: Long): TagEntity? {
    val stmt = TagEntity.FACTORY.select_by_id(id)
    return readable.rawQuery(stmt.statement, stmt.args)
        .mapToOne { TagEntity.FACTORY.select_by_idMapper().map(it) }
  }

  fun searchTagByName(readable: SQLiteDatabase, name: String): List<TagEntity> {
    val stmt = TagEntity.FACTORY.search_by_name(name)
    return readable.rawQuery(stmt.statement, stmt.args)
        .mapToList { TagEntity.FACTORY.search_by_nameMapper().map(it) }
  }

  fun insertTweetTagRelation(writable: SQLiteDatabase, tweetId: Long, tagId: Long) {
    TweetTagRelationModel.Insert_or_ignore(writable).run {
      bind(tweetId, tagId)
      program.executeInsert()
    }
  }

  fun deleteTweetTagRelation(writable: SQLiteDatabase, tweetId: Long, tagId: Long) {
    TweetTagRelationModel.Delete_(writable).run {
      bind(tweetId, tagId)
      program.executeUpdateDelete()
    }
  }

  fun selectAllTweetTagRelations(readable: SQLiteDatabase): List<TweetTagRelation> {
    val stmt = TweetTagRelation.FACTORY.select_all()
    return readable.rawQuery(stmt.statement, stmt.args)
        .mapToList { TweetTagRelation.FACTORY.select_allMapper().map(it) }
  }
}
