package com.egugue.licol.data.sqlite.likedtweet

import com.google.gson.reflect.TypeToken
import com.egugue.licol.data.sqlite.lib.GsonProvider
import com.egugue.licol.data.sqlite.LikedTweetModel
import com.egugue.licol.domain.tweet.Url
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.tweet.media.Video
import com.squareup.sqldelight.ColumnAdapter

/**
 * An entity which has only liked_tweet table's data.
 *
 * An actual tweet has also a info about a user who tweeted.
 * But like_tweet table has only user identifier, not the whole user information.
 * That's why this class has also only user identifier.
 *
 * @see [FullLikedTweetEntity]
 */
data class LikedTweetEntity(
    val id: Long,
    val userId: Long,
    val text: String,
    val imageList: List<Photo>,
    val urlList: List<Url>,
    val video: Video?,
    val created: Long
) : LikedTweetModel {

  override fun id(): Long = id
  override fun user_id(): Long = userId
  override fun text(): String = text
  override fun image_list(): List<Photo> = imageList
  override fun created(): Long = created
  override fun url_list(): List<Url> = urlList
  override fun video(): Video? = video

  companion object {

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