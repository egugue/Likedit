package com.egugue.licol.data.sqlite.likedtweet

import com.egugue.licol.data.sqlite.IdMap
import com.egugue.licol.data.sqlite.fullTweetEntity
import com.egugue.licol.data.sqlite.quotedTweetEntity
import com.egugue.licol.data.sqlite.tweetTagRelation
import com.google.common.truth.Truth.assertThat
import com.egugue.licol.likedTweet
import com.egugue.licol.quotedTweet
import org.junit.Test

class FullLikedTweetEntityTest {

  @Test fun `convert into LikedTweet`() {
    val idMap = IdMap.basedOnTweetId(listOf(
        tweetTagRelation(tweetId = 1, tagId = 1),
        tweetTagRelation(tweetId = 1, tagId = 2)
    ))
    val entity = fullTweetEntity(
        quotedTweet = quotedTweetEntity()
    )

    val actual = entity.toLikedTweet(idMap)

    actual.run {
      assertThat(id).isEqualTo(entity.tweet.id)
      assertThat(userId).isEqualTo(entity.tweet.userId)
      assertThat(quoted?.id).isEqualTo(entity.tweet.quotedTweetId)
      assertThat(text).isEqualTo(entity.tweet.text)
      assertThat(createdAt).isEqualTo(entity.tweet.created)
      assertThat(photoList).isEqualTo(entity.tweet.imageList)
      assertThat(urlList).isEqualTo(entity.tweet.urlList)
    }
  }

  @Test fun `create from LikedTweet`() {
    val liked = likedTweet(quoted = quotedTweet())

    val actual = FullLikedTweetEntity.fromLikedTweet(liked)

    actual.run {
      assertThat(tweet.id).isEqualTo(liked.id)
      assertThat(tweet.userId).isEqualTo(liked.userId)
      assertThat(tweet.text).isEqualTo(liked.text)
      assertThat(tweet.created).isEqualTo(liked.createdAt)
      assertThat(tweet.imageList).isEqualTo(liked.photoList)
      assertThat(tweet.urlList).isEqualTo(liked.urlList)
      assertThat(tweet.video).isEqualTo(liked.video)
      assertThat(tweet.quotedTweetId).isEqualTo(liked.quoted?.id)
    }
  }

}