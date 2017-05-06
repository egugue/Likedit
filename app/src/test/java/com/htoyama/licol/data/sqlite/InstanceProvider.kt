package com.htoyama.licol.data.sqlite

import com.htoyama.licol.data.sqlite.lib.SqliteOpenHelper
import com.htoyama.licol.data.sqlite.likedtweet.LikedTweetSqliteDao
import com.htoyama.licol.data.sqlite.likedtweet.LikedTweetTableGateway
import com.htoyama.licol.data.sqlite.relation.TweetTagRelationTableGateway
import com.htoyama.licol.data.sqlite.tag.TagSqliteDao
import com.htoyama.licol.data.sqlite.tag.TagTableGateway
import com.htoyama.licol.data.sqlite.user.UserSqliteDao
import com.htoyama.licol.data.sqlite.user.UserTableGateway
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import org.robolectric.RuntimeEnvironment
import rx.schedulers.Schedulers

fun briteDatabaseForTest(): BriteDatabase =
    SqlBrite.Builder().build().wrapDatabaseHelper(
        SqliteOpenHelper(RuntimeEnvironment.application),
        Schedulers.immediate())

fun likedTweetTableGateway(db: BriteDatabase) = LikedTweetTableGateway(db)
fun userTableGateway(db: BriteDatabase) = UserTableGateway(db)
fun tagTableGateway(db: BriteDatabase) = TagTableGateway(db)
fun tweetTagRelationTableGateway(db: BriteDatabase) = TweetTagRelationTableGateway(db)

fun likedTweetSqliDao(
    db: BriteDatabase,
    likedTweetTableGateway: LikedTweetTableGateway = likedTweetTableGateway(db),
    tweetTagRelationTableGateway: TweetTagRelationTableGateway = tweetTagRelationTableGateway(db)
) = LikedTweetSqliteDao(
    db,
    likedTweetTableGateway,
    tweetTagRelationTableGateway)

fun userSqliteDao(db: BriteDatabase) = UserSqliteDao(userTableGateway(db))

fun tagSqliteDao(db: BriteDatabase) = TagSqliteDao(
    db,
    tagTableGateway(db),
    tweetTagRelationTableGateway(db)
)
