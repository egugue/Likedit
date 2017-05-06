package com.htoyama.licol.data.sqlite

import org.junit.Test
import com.google.common.truth.Truth.assertThat

class IdMapTest {

  @Test fun `create instance based on tweet id`() {
    val relationsList = listOf(
        tweetTagRelation(tweetId = 1, tagId = 1),
        tweetTagRelation(tweetId = 1, tagId = 2),
        tweetTagRelation(tweetId = 3, tagId = 1)
    )

    val actual = IdMap.basedOnTweetId(relationsList)

    assertThat(actual.getOrEmptyList(1)).containsExactly(1L, 2L)
    assertThat(actual.getOrEmptyList(3)).containsExactly(1L)
  }

  @Test fun `get empty list if the given key(tweet id) is not present`() {
    val actual = IdMap.basedOnTweetId(emptyList()).getOrEmptyList(-1L)
    assertThat(actual).isEmpty()
  }

  @Test fun `create instance based on tag id`() {
    val relationsList = listOf(
        tweetTagRelation(tagId = 1, tweetId = 1),
        tweetTagRelation(tagId = 1, tweetId = 2),
        tweetTagRelation(tagId = 3, tweetId = 1)
    )

    val actual = IdMap.basedOnTagId(relationsList)

    assertThat(actual.getOrEmptyList(1)).containsExactly(1L, 2L)
    assertThat(actual.getOrEmptyList(3)).containsExactly(1L)
  }

  @Test fun `get empty list if the given key(tag id) is not present`() {
    val actual = IdMap.basedOnTagId(emptyList()).getOrEmptyList(-1L)
    assertThat(actual).isEmpty()
  }
}