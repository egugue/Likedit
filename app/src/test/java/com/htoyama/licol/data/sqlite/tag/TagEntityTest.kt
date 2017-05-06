package com.htoyama.licol.data.sqlite.tag

import com.htoyama.licol.data.sqlite.IdMap
import com.htoyama.licol.data.sqlite.tagEntity
import com.htoyama.licol.data.sqlite.tweetTagRelation
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TagEntityTest {

  @Test fun `convert into a Tag`() {
    val tagEntity = tagEntity()
    val idMap = IdMap.basedOnTweetId(listOf(
        tweetTagRelation(tagId = 1, tweetId = 1),
        tweetTagRelation(tagId = 1, tweetId = 2)
    ))

    val actual = tagEntity.toTag(idMap)

    actual.run {
      assertThat(id).isEqualTo(tagEntity.id)
      assertThat(name).isEqualTo(tagEntity.name)
      assertThat(createdAt.time).isEqualTo(tagEntity.created)
    }
  }
}