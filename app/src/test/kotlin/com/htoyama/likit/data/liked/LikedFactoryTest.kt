package com.htoyama.likit.data.liked

import android.util.LongSparseArray
import com.htoyama.likit.TweetBuilder
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tag.TagBuilder
import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.domain.tweet.Tweet
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import java.util.*

@RunWith(RobolectricGradleTestRunner::class)
class LikedFactoryTest {
  lateinit private var factory: LikedFactory

  @Before fun setUp() {
    factory = LikedFactory()
  }

  @Test fun createFrom_whenHavingSameIdAndNotSameId() {
    val tagBuilder = TagBuilder()
    val tagTable = LongSparseArray<List<Tag>>(2)
    val tagList1 = Arrays.asList(tagBuilder.build())
    val tagList2 = Arrays.asList(tagBuilder.build())
    tagTable.append(1, tagList1)
    tagTable.append(2, tagList2)

    val tweetBuilder = TweetBuilder()
    val tweetList = Arrays.asList(
        tweetBuilder.setId(1).build(),
        //skip id-2
        tweetBuilder.setId(3).build())

    val actual = factory.createFrom(tagTable, tweetList)

    assertThat(actual).hasSize(1)
    val liked = actual[0]
    assertThat(liked.tweet.id).isEqualTo(1)
    assertThat(liked.tagList).isEqualTo(tagList1)
  }

  @Test fun createFrom_whenTagTableIsEmpty() {
    val tagTable = LongSparseArray<List<Tag>>()
    val tweetList = Arrays.asList(
        TweetBuilder().setId(1).build())

    val actual = factory.createFrom(tagTable, tweetList)

    assertThat(actual).isEmpty()
  }

  @Test fun createFrom_whenTweetListIsEmpty() {
    val tagTable = LongSparseArray<List<Tag>>(1)
    val tagList1 = Arrays.asList(TagBuilder().build())
    tagTable.append(1, tagList1)
    val tweetList = ArrayList<Tweet>()

    val actual = factory.createFrom(tagTable, tweetList)

    assertThat(actual).isEmpty()
  }

}