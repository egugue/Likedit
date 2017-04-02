package com.htoyama.likit.data.sqlite.tag

import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.data.sqlite.lib.SqliteOpenHelper
import com.htoyama.likit.data.sqlite.tagEntity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TagTableGatewayTest {
  lateinit var gateway: TagTableGateway
  lateinit var helper: SqliteOpenHelper

  @Before fun setUp() {
    helper = SqliteOpenHelper(RuntimeEnvironment.application)
    gateway = TagTableGateway(helper)
  }

  @After fun tearDown() {
    helper.close()
    RuntimeEnvironment.application.deleteDatabase("likedit")
  }

  @Test fun selectTagById_whenTagInserted() {
    val tagId = gateway.insertTag("foo", 1)
    assertThat(tagId).isNotEqualTo(-1)

    val actual = gateway.selectTagById(tagId)
    assertThat(actual).isEqualTo(tagEntity(tagId, "foo", 1))
  }

  @Test fun selectTagById_whenNoTagInserted() {
    val actual = gateway.selectTagById(1)
    assertThat(actual).isNull()
  }

  @Test fun searchTagByName() {
    gateway.insertTag("bb%_bbbbaacc", 1) // hit
    gateway.insertTag("baaccb%_b", 1) // hit
    gateway.insertTag("aaabbbccc", 1)
    gateway.insertTag("aaaBBBccc", 1)
    gateway.insertTag("aaaB%_Bccc", 1) // hit
    gateway.insertTag("abc", 1)
    gateway.insertTag("aabbc", 1)
    gateway.insertTag("あああいいいううう", 1)

    val actual = gateway.searchTagByName("B%_B")

    assertThat(actual).isEqualTo(listOf(
        tagEntity(5, "aaaB%_Bccc", 1),
        tagEntity(2, "baaccb%_b", 1),
        tagEntity(1, "bb%_bbbbaacc", 1)
    ))
  }

  @Test fun searchTagNameName_multiByte() {
    gateway.insertTag("あああいいいううう", 1)
    gateway.insertTag("いいいいいいあああいいいううう", 1)
    gateway.insertTag("いいあああ", 1)
    gateway.insertTag("いいああ", 1)
    gateway.insertTag("ああいいうう", 1)
    gateway.insertTag("あｓｆｄｓｆｄｆｓ", 1)

    val actual = gateway.searchTagByName("あああ")

    assertThat(actual).isEqualTo(listOf(
        tagEntity(1, "あああいいいううう", 1),
        tagEntity(3, "いいあああ", 1),
        tagEntity(2, "いいいいいいあああいいいううう", 1)
    ))
  }

  @Test fun updateTagName() {
    val id = gateway.insertTag("before update", 1)
    gateway.updateTagNameById(id, "after update")

    val actual = gateway.selectTagById(id)

    assertThat(actual).isEqualTo(tagEntity(id, "after update", 1))
  }

  @Test fun updateTagName_whenInvalidIdSpecified() {
    try {
      gateway.updateTagNameById(1, "the tag with the id has not yet inserted")
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessage(
          "tried to update the name of the tag with id(1). but it has not inserted.")
    }
  }

  @Test fun deleteTagById() {
    val id = gateway.insertTag("any", 1)

    val beforeDelete = gateway.selectTagById(id)
    assertThat(beforeDelete).isNotNull()

    gateway.deleteTagById(id)

    val afterDelete = gateway.selectTagById(id)
    assertThat(afterDelete).isNull()
  }

  @Test fun deleteTagById_whenInvaildIdSpecified() {
    try {
      gateway.deleteTagById(1)
      fail()
    } catch (e: IllegalStateException) {
      assertThat(e).hasMessage("tried to delete the tag with id(1). but there is no such tag.")
    }
  }
}