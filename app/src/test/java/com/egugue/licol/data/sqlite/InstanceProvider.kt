package com.egugue.licol.data.sqlite

import com.egugue.licol.data.sqlite.lib.SqliteOpenHelper
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetSqliteDao
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetTableGateway
import com.egugue.licol.data.sqlite.relation.TweetTagRelationTableGateway
import com.egugue.licol.data.sqlite.tag.TagSqliteDao
import com.egugue.licol.data.sqlite.tag.TagTableGateway
import com.egugue.licol.data.sqlite.user.UserSqliteDao
import com.egugue.licol.data.sqlite.user.UserTableGateway
import com.squareup.sqlbrite2.BriteDatabase
import com.squareup.sqlbrite2.SqlBrite
import io.reactivex.schedulers.Schedulers
import org.robolectric.RuntimeEnvironment

fun briteDatabaseForTest(): BriteDatabase =
    SqlBrite.Builder().build().wrapDatabaseHelper(
        SqliteOpenHelper(RuntimeEnvironment.application),
        Schedulers.trampoline())

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

fun userSqliteDao(db: BriteDatabase) = UserSqliteDao(userTableGateway(db), likedTweetTableGateway(db))

fun tagSqliteDao(db: BriteDatabase) = TagSqliteDao(
    db,
    tagTableGateway(db),
    tweetTagRelationTableGateway(db)
)
