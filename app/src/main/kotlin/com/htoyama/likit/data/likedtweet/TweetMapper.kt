package com.htoyama.likit.data.likedtweet

import com.htoyama.likit.domain.tweet.Tweet
import com.htoyama.likit.domain.tweet.Url
import com.htoyama.likit.domain.tweet.media.Photo
import com.htoyama.likit.domain.tweet.media.Video
import com.htoyama.likit.domain.user.User
import com.twitter.sdk.android.core.models.MediaEntity
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton
import com.twitter.sdk.android.core.models.Tweet as Dto

/**
 * Transform between [Tweet] and DTO that represents JSON.
 */
@Singleton
class TweetMapper @Inject constructor() {

  companion object {
    private val DATE_TIME_RFC822 = SimpleDateFormat(
        "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
  }

  fun createListFrom(dtoList: List<com.twitter.sdk.android.core.models.Tweet>): List<Tweet> {
    val tweetList = ArrayList<Tweet>(dtoList.size)
    for (dto in dtoList) {
      tweetList.add(createFrom(dto))
    }

    return Collections.unmodifiableList(tweetList)
  }

  fun createFrom(dto1: com.twitter.sdk.android.core.models.Tweet): Tweet {
    var dto = dto1
    if (dto1.retweetedStatus != null) {
      dto = dto1.retweetedStatus;
    }

    val mediaList = extractAllMedias(dto)
    val photoList = extractPhoto(mediaList)
    val video = extractVideo(mediaList)
    val user = extractUser(dto.user)
    val text = hoge(dto)
    val urlsList = extractUrl(dto)
    val createdAt = parseTimestamp(dto.createdAt)

    return Tweet(
        id = dto.id,
        user = user,
        createdAt = createdAt,
        text = text,
        photoList = photoList,
        urlList = urlsList,
        video = video)
  }

  private fun parseTimestamp(createdAt: String): Long {
    return DATE_TIME_RFC822.parse(createdAt).time
  }

  fun extractUrl(dto: com.twitter.sdk.android.core.models.Tweet): List<Url> {
    if (dto.entities.urls == null) {
      return ArrayList()
    }

    val list = ArrayList<Url>(dto.entities.urls.size)
    for (u in dto.entities.urls) {
      list.add(Url.from(u))
    }
    return list
  }

  fun hoge(dto: com.twitter.sdk.android.core.models.Tweet): String {
    var text = dto.text

    if (dto.entities.urls != null) {
      for (url in dto.entities.urls) {
        text = Pattern.compile(url.url)
            .matcher(text)
            .replaceAll(url.displayUrl)
      }
    }

    if (dto.entities.media != null) {
      for (url in dto.entities.media) {
        text = Pattern.compile(url.url)
            .matcher(text)
            .replaceAll("")
      }
    }

    return text
  }

  private fun extractUser(dto: com.twitter.sdk.android.core.models.User): User {
    val avatorUrl = dto.profileImageUrlHttps
        .replace("_normal", "_reasonably_small")

    return User(
        id = dto.id,
        name = dto.name,
        screenName = "@" + dto.name,
        avatorUrl = avatorUrl)
  }

  private fun extractVideo(mediaList: List<MediaEntity>): Video? {
    for (media in mediaList) {
      if ("video".equals(media.type)) {
        return Video.from(media)
      }
    }
    return null
  }

  private fun extractPhoto(mediaList: List<MediaEntity>): List<Photo> {
    val photoList = ArrayList<Photo>(mediaList.size)

    for (media in mediaList) {
      if ("photo".equals(media.type)) {
        photoList.add(Photo.from(media))
      }
    }

    return photoList;
  }

  private fun extractAllMedias(dto: com.twitter.sdk.android.core.models.Tweet): List<MediaEntity> {
    //val size = dto.entities.media + dto.extendedEtities.media
    val mediaList = ArrayList<MediaEntity>()

    if (dto.entities != null && dto.entities.media != null) {
      mediaList.addAll(dto.entities.media)
    }
    if (dto.extendedEtities != null && dto.extendedEtities.media != null) {
      mediaList.addAll(dto.extendedEtities.media)
    }

    return mediaList;
  }

}