package com.htoyama.likit.model.tweet

import com.htoyama.likit.model.tweet.media.Photo
import com.htoyama.likit.model.tweet.media.Video
import com.htoyama.likit.model.user.User
import com.twitter.sdk.android.core.models.MediaEntity
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import com.twitter.sdk.android.core.models.Tweet as Dto

/**
 * Created by toyamaosamuyu on 2016/06/29.
 */
class TweetFactory {

  companion object {
    private val DATE_TIME_RFC822 = SimpleDateFormat(
        "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
  }

  fun from(dto1: Dto): Tweet {
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
        photos = photoList,
        urls = urlsList,
        video = video)
  }

  private fun parseTimestamp(createdAt: String): Long {
    return DATE_TIME_RFC822.parse(createdAt).time
  }

  fun extractUrl(dto: Dto): List<Url> {
    if (dto.entities.urls == null) {
      return ArrayList()
    }

    val list = ArrayList<Url>(dto.entities.urls.size)
    for (u in dto.entities.urls) {
      list.add(Url.from(u))
    }
    return list
  }

  fun hoge(dto: Dto): String {
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

  private fun extractAllMedias(dto: Dto): List<MediaEntity> {
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