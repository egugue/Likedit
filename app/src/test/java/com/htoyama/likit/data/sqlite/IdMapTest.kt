package com.htoyama.likit.data.sqlite

import org.junit.Test
import com.google.common.truth.Truth.assertThat

class IdMapTest {

  @Test fun `create instance based on tweet id`() {
    val relationsList = listOf(
        tweetTagRelation(tweetId = 1, tagId = 1),
        tweetTagRelation(tweetId = 1, tagId = 2),
        tweetTagRelation(tweetId = 2, tagId = 1)
    )

    val actual = IdMap.basedOnTweetId(relationsList)

    assertThat(actual.getOrEmptyList(1))
        .containsExactly(1L, 2L)

    assertThat(actual.getOrEmptyList(2))
        .containsExactly(1L)
  }

  @Test fun `get empty list if the given key is not present`() {
    val actual = IdMap.basedOnTweetId(emptyList()).getOrEmptyList(-1L)
    assertThat(actual).isEmpty()
  }
}