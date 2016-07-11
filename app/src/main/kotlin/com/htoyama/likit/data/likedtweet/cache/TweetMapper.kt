package com.htoyama.likit.data.likedtweet.cache

import com.htoyama.likit.data.likedtweet.cache.model.*
import com.htoyama.likit.domain.tweet.Tweet
import com.htoyama.likit.domain.tweet.Url
import com.htoyama.likit.domain.tweet.media.Photo
import com.htoyama.likit.domain.tweet.media.Size
import com.htoyama.likit.domain.tweet.media.Sizes
import com.htoyama.likit.domain.tweet.media.Video
import com.htoyama.likit.domain.user.User
import io.realm.RealmList
import java.util.*
import javax.inject.Inject

/**
 * Transform between [Tweet] and [RealmTweet]
 */
internal class TweetMapper
  @Inject constructor() {

  /**
   * Transform [Tweet] into [RealmTweet]
   */
  fun mapFrom(tweet: Tweet): RealmTweet {
    val user = mapFrom(tweet.user)

    val photoList = RealmList<RealmPhoto>()
    for (photo in tweet.photoList) {
      photoList.add(
          mapFrom(photo))
    }

    val urlList = RealmList<RealmUrl>()
    for (url in tweet.urlList) {
      urlList.add(
          mapFrom(url))
    }

    val video = mapFrom(tweet.video)

    return RealmTweet(
        id = tweet.id,
        user = user,
        text = tweet.text,
        photoList = photoList,
        video = video,
        urlList = urlList,
        createAt = Date(tweet.createdAt)
    )
  }

  /**
   * Transform [RealmTweet] into [Tweet]
   */
  fun mapFrom(realmTweet: RealmTweet): Tweet {
    val user = mapFrom(realmTweet.user)

    val photoList = ArrayList<Photo>(realmTweet.photoList.size)
    for (photo in realmTweet.photoList) {
      photoList.add(
          mapFrom(photo))
    }

    val urlList = ArrayList<Url>(realmTweet.urlList.size)
    for (url in realmTweet.urlList) {
      urlList.add(
          mapFrom(url))
    }

    val video = mapFrom(realmTweet.video)

    return Tweet(
        id = realmTweet.id,
        user = user,
        createdAt = realmTweet.createAt.time,
        text = realmTweet.text,
        photoList = photoList,
        urlList = urlList,
        video = video)
  }

  fun mapFrom(user: User): RealmUser {
    return RealmUser(
        id = user.id,
        name = user.name,
        screenName = user.screenName,
        avatorUrl = user.avatorUrl
    )
  }

  fun mapFrom(realmUser: RealmUser): User {
    return User(
        id = realmUser.id,
        name = realmUser.name,
        screenName = realmUser.screenName,
        avatorUrl = realmUser.avatorUrl
    )
  }

  fun mapFrom(photo: Photo): RealmPhoto {
    return photo.run {
      RealmPhoto(
          url = url,
          medium = mapFrom(sizes.medium)
      )
    }
  }

  fun mapFrom(realmPhoto: RealmPhoto): Photo {
    return Photo(
        url = realmPhoto.url,
        sizes = Sizes(
            medium = mapFrom(realmPhoto.medium)
        )
    )
  }

  fun mapFrom(url: Url): RealmUrl {
    return RealmUrl(
        url = url.url,
        displayUrl = url.displayUrl,
        start = url.start,
        end = url.end)
  }

  fun mapFrom(realmUrl: RealmUrl): Url {
    return Url(
        url = realmUrl.url,
        displayUrl = realmUrl.displayUrl,
        start = realmUrl.start,
        end = realmUrl.end)
  }

  fun mapFrom(video: Video?): RealmVideo? {
    return video?.run {
      RealmVideo(
          url = url,
          medium = mapFrom(sizes.medium)
      )
    }
  }

  fun mapFrom(realmVideo: RealmVideo?): Video? {
    return realmVideo?.run {
      Video(
          url = url,
          sizes = Sizes(
              medium = mapFrom(medium))
      )
    }
  }

  fun mapFrom(size: Size): RealmSize {
    return RealmSize(
        width = size.width,
        height = size.height)
  }

  fun mapFrom(realmSizse: RealmSize): Size {
    return Size(
        width = realmSizse.width,
        height = realmSizse.height)
  }

}