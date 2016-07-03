package com.htoyama.likit.data.tweet.cache

import com.htoyama.likit.data.tweet.cache.model.*
import com.htoyama.likit.domain.tweet.Tweet
import com.htoyama.likit.domain.tweet.Url
import com.htoyama.likit.domain.tweet.media.Photo
import com.htoyama.likit.domain.tweet.media.Size
import com.htoyama.likit.domain.tweet.media.Sizes
import com.htoyama.likit.domain.tweet.media.Video
import com.htoyama.likit.domain.user.User
import io.realm.Realm
import io.realm.RealmList
import io.realm.Sort
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * Created by toyamaosamuyu on 2016/07/03.
 */
class LikedTweetCacheDao @Inject constructor() {

  fun store(likedList: List<Tweet>) {
    Realm.getDefaultInstance().use {
      it.executeTransaction {
        val realmLikedList = ArrayList<RealmTweet>(likedList.size)
        for (tweet in likedList) {
          realmLikedList.add(
              mapFrom(tweet))
        }
        it.copyToRealmOrUpdate(realmLikedList)
      }
    }
  }

  private fun mapFrom(tweet: Tweet): RealmTweet {
    val urlList = RealmList<RealmUrl>()
    for (url in tweet.urls) {
      urlList.add(RealmUrl(
          url = url.url,
          displayUrl = url.displayUrl,
          start = url.start,
          end = url.end
      ))
    }

    val photoList = RealmList<RealmPhoto>()
    for (photo in tweet.photos) {
      photoList.add(RealmPhoto(
          url = photo.url,
          medium = RealmSize(
              width = photo.sizes.medium.width,
              height = photo.sizes.medium.height)
      ))
    }

    val user = RealmUser(
        name = tweet.user.name,
        screenName = tweet.user.screenName,
        avatorUrl = tweet.user.avatorUrl
    )

    val video = tweet.video?.run {
      RealmVideo(
          url = url,
          medium = RealmSize(
              width = sizes.medium.width,
              height = sizes.medium.height))
    }

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

  fun getList(page: Int, count: Int): Observable<List<Tweet>> {
    Realm.getDefaultInstance().use {
      val allResults = it.where(RealmTweet::class.java)
          .findAllSorted("id", Sort.DESCENDING)

      if (allResults.isEmpty()) {
        return Observable.fromCallable {
          Collections.unmodifiableList(ArrayList<Tweet>())
        }
      }

      val fromIndex = (page - 1) * count
      val toIndex = fromIndex + count
      val results: List<RealmTweet>
      if (toIndex > allResults.size) {
        results = allResults.subList(fromIndex, allResults.size)
      } else {
        results = allResults.subList(fromIndex, fromIndex + count)
      }

      val favoriteList = ArrayList<Tweet>(results.size)
      for (realmTweet in results) {
        favoriteList.add(
            mapFrom(realmTweet))
      }

      return Observable.fromCallable {
        Collections.unmodifiableList(favoriteList)
      }
    }
  }

  private fun mapFrom(realmTweet: RealmTweet): Tweet {

    val photoList = ArrayList<Photo>(realmTweet.photoList.size)
    for (realmPhoto in realmTweet.photoList) {
      photoList.add(Photo(
          url = realmPhoto.url,
          sizes = Sizes(
              medium = Size(
                  width = realmPhoto.medium.width,
                  height = realmPhoto.medium.height
                  )
          )
      ))
    }

    val video = realmTweet.video?.run {
      Video(
          url = this.url,
          sizes = Sizes(
              medium = Size(
                  width =  medium.width,
                  height = medium.height)
          )
      )
    }

    val user = User(
        name = realmTweet.user.name,
        screenName = realmTweet.user.screenName,
        avatorUrl = realmTweet.user.avatorUrl
    )

    val urlList = ArrayList<Url>(realmTweet.urlList.size)
    for (realmUrl in realmTweet.urlList) {
      urlList.add(Url(
          url = realmUrl.url,
          displayUrl = realmUrl.displayUrl,
          start = realmUrl.start,
          end = realmUrl.end
      ))
    }

    return Tweet(
        id = realmTweet.id,
        user = user,
        createdAt = realmTweet.createAt.time,
        text = realmTweet.text,
        photos = photoList,
        urls = urlList,
        video = video)
  }

}