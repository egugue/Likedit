package com.htoyama.likit.common.extensions

import com.htoyama.likit.domain.tweet.Url
import com.htoyama.likit.domain.tweet.media.Photo
import com.htoyama.likit.domain.tweet.media.Video
import com.twitter.sdk.android.core.models.MediaEntity
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Extensions for entities in twitter-kit
 */

/* ----- Twitter  ----- */

/**
 * Extract all media as [MediaEntity].
 */
fun Tweet.extractAllMedias(): List<MediaEntity> {
  //val size = this.entities.media + this.extendedEtities.media
  val mediaList = ArrayList<MediaEntity>()

  if (entities != null && entities.media != null) {
    mediaList.addAll(entities.media)
  }
  if (extendedEtities != null && extendedEtities.media != null) {
    mediaList.addAll(extendedEtities.media)
  }

  return mediaList
}

/**
 * Extract all urls as [Url].
 */
fun Tweet.extractUrlList(): List<Url> {
  if (entities.urls == null) {
    return emptyList()
  }
  return entities.urls.map { Url.from(it) }
}

/**
 * Convert a text and something related to it into a text for display
 */
fun Tweet.textForDisplay(): String {
  var text = this.text

  if (entities.urls != null) {
    for (url in entities.urls) {
      text = Pattern.compile(url.url)
          .matcher(text)
          .replaceAll(url.displayUrl)
    }
  }

  if (entities.media != null) {
    for (url in entities.media) {
      text = Pattern.compile(url.url)
          .matcher(text)
          .replaceAll("")
    }
  }

  return text
}

val DATE_TIME_RFC822 = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH)

/**
 * Represent a created time in it into as Long type
 */
fun Tweet.createdToLong(): Long = DATE_TIME_RFC822.parse(createdAt).time


/* ----- User  ----- */

/**
 * Extract an avatar url
 */
fun User.avatarUrl(): String = profileImageUrlHttps.replace("_normal", "_reasonably_small")


/* ----- MediaEntity  ----- */

/**
 * Extract all photos as [Photo]
 */
fun List<MediaEntity>.extractPhotoList(): List<Photo> =
    this
        .filter { "photo" == it.type }
        .map { Photo.from(it) }

/**
 * Extract a video as [Video]
 */
fun List<MediaEntity>.extractVideo(): Video? =
    this
        .filter { "video" == it.type }
        .map { Video.from(it) }
        .firstOrNull()
