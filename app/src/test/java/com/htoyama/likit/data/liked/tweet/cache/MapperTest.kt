package com.htoyama.likit.data.liked.tweet.cache

import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.*
import com.htoyama.likit.domain.tweet.media.Video
import io.realm.RealmList
import java.util.*

class MapperTest {
  private lateinit var mapper: TweetMapper

  @Before fun setUp() {
    mapper = TweetMapper()
  }

  @Test fun fromTweet() {
    val tweet = TweetBuilder().build()
    val realmTweet = mapper.mapFrom(tweet)

    realmTweet.run {
      assertThat(id).isEqualTo(tweet.id)
      assertThat(createAt.time).isEqualTo(tweet.createdAt)
      assertThat(text).isEqualTo(tweet.text)
      assertThat(photoList.size).isEqualTo(tweet.photoList.size)
      assertThat(urlList.size).isEqualTo(tweet.urlList.size)
    }
  }

  @Test fun fromTweet_whenPhotoListIsEmpty() {
    val tweet = TweetBuilder()
        .setPhotoList(Collections.emptyList())
        .build()
    val realmTweet = mapper.mapFrom(tweet)

    assertThat(realmTweet.photoList).isEmpty()
  }

  @Test fun fromTweet_whenUrlListIsEmpty() {
    val tweet = TweetBuilder()
        .setUrlList(Collections.emptyList())
        .build()
    val realmTweet = mapper.mapFrom(tweet)

    assertThat(realmTweet.urlList).isEmpty()
  }

  @Test fun fromRealmTweet() {
    val realmTweet = RealmTweet()
    val tweet = mapper.mapFrom(realmTweet)

    tweet.run {
      assertThat(id).isEqualTo(realmTweet.id)
      assertThat(createdAt).isEqualTo(realmTweet.createAt.time)
      assertThat(text).isEqualTo(realmTweet.text)
      assertThat(photoList.size).isEqualTo(realmTweet.photoList.size)
      assertThat(urlList.size).isEqualTo(realmTweet.urlList.size)
    }
  }

  @Test fun fromRealmTweet_whenPhotoListtIsEmpty() {
    val realmTweet = RealmTweet(photoList = RealmList())
    val tweet = mapper.mapFrom(realmTweet)
    assertThat(tweet.photoList).isEmpty()
  }

  @Test fun fromRealmTweet_whenUrlListIsEmpty() {
    val realmTweet = RealmTweet(urlList = RealmList())
    val tweet = mapper.mapFrom(realmTweet)
    assertThat(tweet.urlList).isEmpty()
  }

  @Test fun fromUser() {
    val user = UserBuilder().build()
    val realmUser = mapper.mapFrom(user)

    realmUser.run {
      assertThat(id).isEqualTo(user.id)
      assertThat(name).isEqualTo(user.name)
      assertThat(screenName).isEqualTo(user.screenName)
      assertThat(avatorUrl).isEqualTo(user.avatorUrl)
    }
  }

  @Test fun fromRealmUser() {
    val realmUser = RealmUser()
    val user = mapper.mapFrom(realmUser)

    user.run {
      assertThat(id).isEqualTo(realmUser.id)
      assertThat(name).isEqualTo(realmUser.name)
      assertThat(screenName).isEqualTo(realmUser.screenName)
      assertThat(avatorUrl).isEqualTo(realmUser.avatorUrl)
    }
  }

  @Test fun fromPhoto() {
    val photo = PhotoBuilder().build()
    val realmPhoto = mapper.mapFrom(photo)

    realmPhoto.run {
      assertThat(url).isEqualTo(photo.url)
      assertThat(medium.width).isEqualTo(photo.sizes.medium.width)
      assertThat(medium.height).isEqualTo(photo.sizes.medium.height)
    }
  }

  @Test fun fromRealmPhoto() {
    val realmPhoto = RealmPhoto()
    val photo = mapper.mapFrom(realmPhoto)

    photo.run {
      assertThat(url).isEqualTo(realmPhoto.url)
      sizes.medium.run {
        assertThat(width).isEqualTo(realmPhoto.medium.width)
        assertThat(height).isEqualTo(realmPhoto.medium.height)
      }
    }
  }

  @Test fun fromUrl() {
    val url = UrlBuilder().build()
    val realmUrl = mapper.mapFrom(url)

    realmUrl.run {
      assertThat(this.url).isEqualTo(url.url)
      assertThat(displayUrl).isEqualTo(url.displayUrl)
      assertThat(start).isEqualTo(url.start)
      assertThat(end).isEqualTo(url.end)
    }
  }

  @Test fun fromRealmUrl() {
    val realmUrl = RealmUrl()
    val url = mapper.mapFrom(realmUrl)

    url.run {
      assertThat(this.url).isEqualTo(realmUrl.url)
      assertThat(displayUrl).isEqualTo(realmUrl.displayUrl)
      assertThat(start).isEqualTo(realmUrl.start)
      assertThat(end).isEqualTo(realmUrl.end)
    }
  }

  @Test fun fromVideo() {
    val video = VideoBuilder().build()
    val realmVideo = mapper.mapFrom(video)

    assertThat(realmVideo).isNotNull()

    realmVideo!!.run {
      assertThat(url).isEqualTo(realmVideo.url)
      assertThat(medium.width).isEqualTo(video.sizes.medium.width)
      assertThat(medium.height).isEqualTo(video.sizes.medium.height)
    }
  }

  @Test fun fromVideo_ButNull() {
    val video: Video? = null
    val realmVideo = mapper.mapFrom(video)
    assertThat(realmVideo).isNull()
  }

  @Test fun fromRealmVideo() {
    val realmVideo = RealmVideo()
    val video = mapper.mapFrom(realmVideo)

    assertThat(video).isNotNull()

    video!!.run {
      assertThat(url).isEqualTo(video.url)
      sizes.medium.run {
        assertThat(width).isEqualTo(video.sizes.medium.width)
        assertThat(height).isEqualTo(video.sizes.medium.height)
      }
    }
  }

  @Test fun fromRealmVideo_ButNull() {
    val realmVideo: RealmVideo? = null
    val video = mapper.mapFrom(realmVideo)

    assertThat(video).isNull()
  }

}