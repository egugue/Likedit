package com.egugue.licol.data.sqlite.likedtweet

import com.google.gson.reflect.TypeToken
import com.egugue.licol.data.sqlite.lib.GsonProvider
import com.egugue.licol.data.sqlite.LikedTweetModel
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tweet.Url
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.tweet.media.Video
import com.squareup.sqldelight.ColumnAdapter

/**
 * An entity which has only liked_tweet table's data.
 *
 * An actual tweet has also a info about a quoted tweet.
 * But liked_tweet table has only quoted tweet's identifier, not the whole quoted tweet information.
 * That's why this class has also only quoted tweet's identifier.
 *
 * @see [FullLikedTweetEntity]
 */
data class LikedTweetEntity(
    val id: Long,
    val userId: Long,
    val text: String,
    val likedCount: Int,
    val imageList: List<Photo>,
    val urlList: List<Url>,
    val video: Video?,
    val quotedTweetId: Long?,
    val created: Long
) : LikedTweetModel {

  override fun id(): Long = id
  override fun user_id(): Long = userId
  override fun text(): String = text
  override fun liked_count(): Int = likedCount
  override fun image_list(): List<Photo> = imageList
  override fun url_list(): List<Url> = urlList
  override fun video(): Video? = video
  override fun quoted_tweet_id(): Long? = quotedTweetId
  override fun created(): Long = created

  companion object {

    fun fromLikedTweet(t: LikedTweet): LikedTweetEntity {
      return LikedTweetEntity(
          t.id,
          t.userId,
          t.text,
          t.likedCount,
          t.photoList,
          t.urlList,
          t.video,
          t.quoted?.id,
          t.createdAt)
    }

    val PHOTO_LIST_ADAPTER = object : ColumnAdapter<List<Photo>, String> {
      private val type = object : TypeToken<List<Photo>>() {}.type

      override fun decode(databaseValue: String): List<Photo> =
          GsonProvider.gson.fromJson<List<Photo>>(
              databaseValue, type)

      override fun encode(value: List<Photo>): String = GsonProvider.gson.toJson(value)
    }

    val URL_LIST_ADAPTER = object : ColumnAdapter<List<Url>, String> {
      private val type = object : TypeToken<List<Url>>() {}.type

      override fun decode(databaseValue: String): List<Url> =
          GsonProvider.gson.fromJson<List<Url>>(
              databaseValue, type)

      override fun encode(value: List<Url>): String = GsonProvider.gson.toJson(value)
    }

    val VIDEO_ADAPTER = object : ColumnAdapter<Video, String> {

      override fun decode(databaseValue: String): Video =
          GsonProvider.gson.fromJson(
              databaseValue, Video::class.java)

      override fun encode(value: Video): String = GsonProvider.gson.toJson(value)
    }

    @JvmField val FACTORY: LikedTweetModel.Factory<LikedTweetEntity> = LikedTweetModel.Factory(
        LikedTweetModel.Creator<LikedTweetEntity>(::LikedTweetEntity),
        PHOTO_LIST_ADAPTER,
        URL_LIST_ADAPTER,
        VIDEO_ADAPTER)
  }
}