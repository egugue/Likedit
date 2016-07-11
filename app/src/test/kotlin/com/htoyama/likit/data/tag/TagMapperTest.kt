package com.htoyama.likit.data.tag

import com.htoyama.likit.domain.tag.TagBuilder
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

/**
 * Created by toyamaosamuyu on 2016/07/12.
 */
class TagMapperTest {
  lateinit internal var mapper: TagMapper

  @Before fun setUp() {
    mapper = TagMapper()
  }

  @Test fun mapFromTag() {
    val tag = TagBuilder().build()
    val realmTag = mapper.mapFrom(tag)

    realmTag.run {
      assertThat(id).isEqualTo(tag.id)
      assertThat(name).isEqualTo(tag.name)
      assertThat(createdAt).isEqualTo(tag.createdAt)
    }
  }

  @Test fun mapFromRealmTag() {
    val realmTag = RealmTag()
    val tag = mapper.mapFrom(realmTag)

    tag.run {
      assertThat(id).isEqualTo(realmTag.id)
      assertThat(name).isEqualTo(realmTag.name)
      assertThat(createdAt).isEqualTo(realmTag.createdAt)
    }
  }
}