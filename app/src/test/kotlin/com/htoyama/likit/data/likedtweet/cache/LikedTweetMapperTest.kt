package com.htoyama.likit.data.likedtweet.cache

import com.htoyama.likit.TweetBuilder
import com.htoyama.likit.data.likedtweet.cache.model.RealmTweet
import com.htoyama.likit.data.tag.TagMapper
import com.htoyama.likit.domain.likedtweet.LikedTweet
import com.htoyama.likit.domain.tag.Tag
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when` as When
import java.util.*
import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.data.likedtweet.cache.model.RealmLikedTweet
import com.htoyama.likit.data.tag.RealmTag
import io.realm.RealmList
import org.junit.Ignore
import org.mockito.Mockito.mock

class LikedTweetMapperTest {
  lateinit private var mapper: LikedTweetMapper
  lateinit private var tweetMapper: TweetMapper
  lateinit private var tagMapper: TagMapper

  @Before fun setUp() {
    tweetMapper = mock(TweetMapper::class.java)
    tagMapper = mock(TagMapper::class.java)
    mapper = LikedTweetMapper(tweetMapper, tagMapper)
  }

  @Ignore("cannot use Mockito.when")
  @Test fun mapFromLiked_shouldTransformTweetIntoRealmTweet() {
    mapper = LikedTweetMapper(mock(TweetMapper::class.java), tagMapper)
    val tweet = TweetBuilder().build()
    val expectedRealmTweet = RealmTweet()
    val liked = LikedTweet(
        tweet = tweet,
        tagList = ArrayList<Tag>())

    When(tweetMapper.mapFrom(tweet))
        .thenReturn(expectedRealmTweet)

    val actual = mapper.mapFrom(liked)

    assertThat(actual.tweetId).isEqualTo(expectedRealmTweet)
  }

  @Test fun mapFromLiked_shouldTransformTagListWhenListIsEmpty() {
    mapper = LikedTweetMapper(tweetMapper, TagMapper())
    val tagList = ArrayList<Tag>()
    val liked = LikedTweet(
        tweet = TweetBuilder().build(),
        tagList = tagList)

    /*
    When(tagMapper.mapFrom(any(Tag::class.java)))
        .thenReturn(RealmTag())
        */

    val actual = mapper.mapFrom(liked)

    assertThat(actual.tagList.size)
        .isEqualTo(tagList.size)
  }

  @Test fun mapFromLiked_shouldTransformTagListWhenListIsNotEmpty() {
    mapper = LikedTweetMapper(tweetMapper, TagMapper())
    val tagList = ArrayList<Tag>(3)
    val liked = LikedTweet(
        tweet = TweetBuilder().build(),
        tagList = tagList)

    /*
    When(tagMapper.mapFrom(any(Tag::class.java)))
        .thenReturn(RealmTag())
        */

    val actual = mapper.mapFrom(liked)

    assertThat(actual.tagList.size)
        .isEqualTo(tagList.size)
  }

  @Ignore("cannot use Mockito.when")
  @Test fun mapFromRealmLiked_shouldTransformRealmTweet() {
    mapper = LikedTweetMapper(mock(TweetMapper::class.java), tagMapper)
    val expectedTweet = TweetBuilder().build()
    val realmLiked = RealmLikedTweet()

    When(tweetMapper.mapFrom(realmLiked.tweetId))
        .thenReturn(expectedTweet)

    val actual = mapper.mapFrom(realmLiked)

    assertThat(actual.tweet).isEqualTo(realmLiked)
  }

  @Test fun mapFromRealmLiked_shouldTransformRealmTagListWhenListIsEmpty() {
    mapper = LikedTweetMapper(tweetMapper, TagMapper())
    val realmLiked = RealmLikedTweet()

    /*
    When(tagMapper.mapFrom(any(Tag::class.java)))
        .thenReturn(RealmTag())
        */

    val actual = mapper.mapFrom(realmLiked)

    assertThat(actual.tagList.size)
        .isEqualTo(0)
  }

  @Test fun mapFromRealmLiked_shouldTransformRealmTagListWhenListIsNotEmpty() {
    mapper = LikedTweetMapper(tweetMapper, TagMapper())
    val realmLiked = RealmLikedTweet(
        tagList = RealmList(RealmTag())
    )

    /*
    When(tagMapper.mapFrom(any(Tag::class.java)))
        .thenReturn(RealmTag())
        */

    val actual = mapper.mapFrom(realmLiked)

    assertThat(actual.tagList.size)
        .isEqualTo(1)
  }

}
