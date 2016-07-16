package com.htoyama.likit.data.likedtweet.tweet.net

import com.htoyama.likit.data.likedtweet.cache.model.*
import com.twitter.sdk.android.core.models.MediaEntity
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.UrlEntity
import com.twitter.sdk.android.core.models.User
import io.realm.RealmList
import java.util.*
import javax.inject.Inject

/**
 * Created by toyamaosamuyu on 2016/07/16.
 */
/*
class Mapper @Inject internal constructor() {

  fun mapToRealmTweetFrom(dto: Tweet): RealmTweet {
    val realmUser = mapFrom(dto.user)
    val allMediaList = extractAllMedias(dto)

    val photoList = RealmList<RealmPhoto>()
    allMediaList.filter { "photo".equals(it.type) }
        .forEach { mapToRealmPhotoFrom(it) }

    val realmVideo = mapToRealmVideoFrom(allMediaList)
    val urlList = extractUrl(dto)

    return RealmTweet(
        id = dto.id,
        user = realmUser,
        text = dto.text,
        photoList = photoList,
        video = realmVideo,
        urlList = urlList,
        createAt = Date()
    )
  }

  private fun extractUrl(dto: Tweet): RealmList<RealmUrl> {
    if (dto.entities.urls == null) {
      return RealmList()
    }

    val realmUrlList = RealmList<RealmUrl>()
    dto.entities.urls.forEach {
      realmUrlList.add(mapToRealUrlFrom(it))
    }
    return realmUrlList
  }

  private fun mapToRealUrlFrom(url: UrlEntity): RealmUrl {
    return RealmUrl(
        url = url.url,
        displayUrl = url.displayUrl,
        start = url.start,
        end = url.end)
  }

  private fun mapToRealmVideoFrom(mediaList: List<MediaEntity>): RealmVideo? {
    return mediaList.filter { "video".equals(it.type) }
        .firstOrNull()
        .let {
          it!!

          val sizeMedium = RealmSize(
              it.sizes.medium.w,
              it.sizes.medium.h)

          RealmVideo(
              url = it.mediaUrlHttps,
              medium = sizeMedium
          )
        }
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

  fun mapFrom(user: User): RealmUser {
    return RealmUser(
        id = user.id,
        name = user.name,
        screenName = user.screenName,
        avatorUrl = user.profileImageUrlHttps
    )
  }

  fun mapToRealmPhotoFrom(media: MediaEntity): RealmPhoto {
    val sizeMedium = RealmSize(
        media.sizes.medium.w,
        media.sizes.medium.h)

    return RealmPhoto(
        url = media.mediaUrlHttps,
        medium = sizeMedium)
  }

}
*/
