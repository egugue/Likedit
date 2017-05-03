package com.htoyama.likit.data.sqlite

import com.htoyama.likit.data.sqlite.lib.SqliteOpenHelper
import com.htoyama.likit.data.sqlite.likedtweet.LikedTweetSqliteDao
import com.htoyama.likit.data.sqlite.likedtweet.LikedTweetTableGateway
import com.htoyama.likit.data.sqlite.relation.TweetTagRelationTableGateway
import com.htoyama.likit.data.sqlite.tag.TagSqliteDao
import com.htoyama.likit.data.sqlite.tag.TagTableGateway
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import org.robolectric.RuntimeEnvironment
import rx.schedulers.Schedulers

fun briteDatabaseForTest(): BriteDatabase =
    SqlBrite.Builder().build().wrapDatabaseHelper(
        SqliteOpenHelper(RuntimeEnvironment.application),
        Schedulers.immediate())

fun likedTweetTableGateway(db: BriteDatabase) = LikedTweetTableGateway(db)
fun tagTableGateway(db: BriteDatabase) = TagTableGateway(db)
fun tweetTagRelationTableGateway(db: BriteDatabase) = TweetTagRelationTableGateway(db)

fun likedTweetSqliDao(db: BriteDatabase) = LikedTweetSqliteDao(
        db,
        likedTweetTableGateway(db),
        tweetTagRelationTableGateway(db)
)

fun tagSqliteDao(db: BriteDatabase) = TagSqliteDao(
    db,
    tagTableGateway(db),
    tweetTagRelationTableGateway(db)
)
