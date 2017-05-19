package com.egugue.licol.data.sqlite.lib

import android.database.sqlite.SQLiteDatabase
import com.egugue.licol.data.sqlite.*
import com.egugue.licol.data.sqlite.tag.TagEntity
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetEntity
import com.egugue.licol.data.sqlite.likedtweet.QuotedTweetEntity
import com.egugue.licol.data.sqlite.user.UserEntity
import com.squareup.sqlbrite.BriteDatabase

/**
 * A collection which has a lot of simple SQLite scripts.
 *
 * NOTE:
 * Each script is expected to be used in combination with other class
 * so that they should not close a given [SQLiteDatabase].
 */
//TODO: probably should close writable database in almost all methods. but If so, don't know why but unit test is failed...
internal object SqliteScripts {

  fun insertOrIgnoreIntoLikedTweet(db: BriteDatabase, tweet: LikedTweetEntity) {
    val stmt = LikedTweetModel.Insert_liked_tweet(db.writableDatabase, LikedTweetEntity.FACTORY)
    tweet.apply {
      stmt.bind(id, userId, text, likedCount, imageList, urlList, video, quotedTweetId, created)
    }
    db.executeInsert(stmt.table, stmt.program)
  }

  fun deleteLikedTweetById(db: BriteDatabase, tweetId: Long) {
    val stmt = LikedTweetModel.Delete_by_id(db.writableDatabase)
    stmt.bind(tweetId)
    db.executeUpdateDelete(stmt.table, stmt.program)
  }

  fun insertOrIgnoreQuotedTweet(db: BriteDatabase, quoted: QuotedTweetEntity) {
    val stmt = QuotedTweetModel.Insert_quoted_tweet(db.writableDatabase)
    quoted.apply {
      stmt.bind(id, text, userId, userName, userScreenName, userAvatarUrl)
    }
    db.executeInsert(stmt.table, stmt.program)
  }

  fun insertOrUpdateIntoUser(db: BriteDatabase, user: UserEntity) {
    val stmt = UserModel.Insert_user(db.writableDatabase)
    user.apply {
      stmt.bind(id, name, screenName, avatarUrl)
    }
    db.executeInsert(stmt.table, stmt.program)
  }

  fun insertTag(db: BriteDatabase, name: String, created: Long): Long {
    val stmt = TagModel.Insert_(db.writableDatabase)
    stmt.bind(name, created)
    return db.executeInsert(stmt.table, stmt.program)
  }

  fun updateTagNameById(db: BriteDatabase, name: String, id: Long) {
    val stmt = TagModel.Update_name(db.writableDatabase)
    stmt.bind(name, id)
    db.executeUpdateDelete(stmt.table, stmt.program)
  }

  fun deleteTagById(db: BriteDatabase, id: Long) {
    val stmt = TagModel.Delete_by_id(db.writableDatabase)
    stmt.bind(id)
    db.executeUpdateDelete(stmt.table, stmt.program)
  }

  fun selectTagById(db: BriteDatabase, id: Long): TagEntity? {
    val stmt = TagEntity.FACTORY.select_by_id(id)
    return db.readableDatabase.rawQuery(stmt.statement, stmt.args)
        .mapToOne { TagEntity.FACTORY.select_by_idMapper().map(it) }
  }

  fun insertTweetTagRelation(db: BriteDatabase, tweetId: Long, tagId: Long) {
    val stmt = TweetTagRelationModel.Insert_or_ignore(db.writableDatabase)
    stmt.bind(tweetId, tagId)
    db.executeInsert(stmt.table, stmt.program)
  }

  fun deleteTweetTagRelation(db: BriteDatabase, tweetId: Long, tagId: Long) {
    val stmt = TweetTagRelationModel.Delete_(db.writableDatabase)
    stmt.bind(tweetId, tagId)
    db.executeUpdateDelete(stmt.table, stmt.program)
  }
}
