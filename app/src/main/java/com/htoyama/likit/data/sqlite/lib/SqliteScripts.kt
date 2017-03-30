package com.htoyama.likit.data.sqlite.lib

import android.database.sqlite.SQLiteDatabase
import com.htoyama.likit.data.sqlite.TagModel
import com.htoyama.likit.data.sqlite.TweetModel
import com.htoyama.likit.data.sqlite.UserModel
import com.htoyama.likit.data.sqlite.entity.TweetEntity
import com.htoyama.likit.data.sqlite.entity.FullTweetEntity
import com.htoyama.likit.data.sqlite.entity.TagEntity
import com.htoyama.likit.data.sqlite.entity.UserEntity

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
        .mapToList { TweetEntity.FULL_TWEET_MAPPER.map(it) }
  }

  fun selectTweets(limit: Long, offset: Long, readable: SQLiteDatabase): List<FullTweetEntity> {
    val stmt = TweetEntity.FACTORY.select_tweets(limit, offset)
    return readable.rawQuery(stmt.statement, stmt.args)
        .mapToList { TweetEntity.FULL_TWEET_MAPPER.map(it) }
  }

  fun insertOrUpdateIntoTweet(writable: SQLiteDatabase, tweet: TweetEntity) {
    val stmt = TweetModel.Insert_tweet(writable, TweetEntity.FACTORY)
    tweet.apply {
      stmt.bind(id, userId, text, imageList, urlList, video, created)
    }
    stmt.program.executeInsert()
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
}
