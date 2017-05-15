package com.egugue.licol.data.sqlite.likedtweet

import com.egugue.licol.data.sqlite.IdMap
import com.egugue.licol.data.sqlite.fullTweetEntity
import com.egugue.licol.data.sqlite.tweetTagRelation
import com.google.common.truth.Truth.assertThat
import com.egugue.licol.likedTweet
import org.junit.Test

class FullLikedTweetEntityTest {

  @Test fun `convert into LikedTweet`() {
    val idMap = IdMap.basedOnTweetId(listOf(
        tweetTagRelation(tweetId = 1, tagId = 1),
        tweetTagRelation(tweetId = 1, tagId = 2)
    ))
    val entity = fullTweetEntity()

    val actual = entity.toLikedTweet(idMap)

    actual.run {
      assertThat(tweet.id).isEqualTo(entity.tweet.id)
      assertThat(tweet.text).isEqualTo(entity.tweet.text)
      assertThat(tweet.createdAt).isEqualTo(entity.tweet.created)
      assertThat(tweet.photoList).isEqualTo(entity.tweet.imageList)
      assertThat(tweet.urlList).isEqualTo(entity.tweet.urlList)
      assertThat(tweet.user.id).isEqualTo(entity.tweet.userId)
    }
  }

  @Test fun `create from LikedTweet`() {
    val liked = likedTweet()

    val actual = FullLikedTweetEntity.fromLikedTweet(liked)

    actual.run {
      assertThat(tweet.id).isEqualTo(liked.tweet.id)
      assertThat(tweet.text).isEqualTo(liked.tweet.text)
      assertThat(tweet.created).isEqualTo(liked.tweet.createdAt)
      assertThat(tweet.imageList).isEqualTo(liked.tweet.photoList)
      assertThat(tweet.urlList).isEqualTo(liked.tweet.urlList)
      assertThat(tweet.userId).isEqualTo(liked.tweet.user.id)
    }
  }

}