package com.htoyama.licol.domain.tag

import org.junit.Assert.*
import org.junit.Test
import java.util.*
import com.google.common.truth.Truth.assertThat

class TagTest {

  @Test fun instantiate() {
    val expectedCreatedAt = Date()
    val expectedName = "123456789012345"
    val tag = TagBuilder()
        .setId(1)
        .setName(expectedName)
        .setCreateAt(expectedCreatedAt)
        .build()

    tag.run {
      assertThat(id).isEqualTo(1)
      assertThat(name).isEqualTo(expectedName)
      assertThat(createdAt).isEqualTo(expectedCreatedAt)
    }
  }

  @Test fun instantiate_shouldThrowException_whenNameLengthIsOverMax() {
    val invalidName = "1234567890123456"

    try {
      TagBuilder()
          .setName(invalidName)
          .build()
      fail()
    } catch (expected: IllegalArgumentException) {
      assertThat(expected.message).isEqualTo("name must be within 15 length. but was 16")
    }
  }

  @Test fun setName_shouldThrowException_whenNameLengthIsOverMax() {
    val tag = TagBuilder().build()
    try {
      val invalidName = "1234567890123456"
      tag.name = invalidName
      fail()
    } catch (expected: IllegalArgumentException) {
      assertThat(expected.message).isEqualTo("name must be within 15 length. but was 16")
    }
  }

}