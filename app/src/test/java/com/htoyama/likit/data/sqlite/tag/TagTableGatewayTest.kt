package com.htoyama.likit.data.sqlite.tag

import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.common.None
import com.htoyama.likit.common.Optional
import com.htoyama.likit.common.toOptional
import com.htoyama.likit.data.sqlite.tagEntity
import com.htoyama.likit.testutil.SqliteTestingRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TagTableGatewayTest {
  @Rule @JvmField val rule = SqliteTestingRule()
  lateinit var gateway: TagTableGateway

  @Before fun setUp() {
    gateway = TagTableGateway(rule.briteDB)
  }

  @Test fun selectTagById_whenTagInserted() {
    val tagId = gateway.insertTag("foo", 1)
    assertThat(tagId).isNotEqualTo(-1)

    val actual = gateway.selectTagById(tagId).test()
    actual.assertValue(tagEntity(tagId, "foo", 1).toOptional())
  }

  @Test fun selectTagById_whenNoTagInserted() {
    val actual = gateway.selectTagById(1).test()
    actual.assertValue(None as Optional<TagEntity>)
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

    val actual = gateway.searchTagByName("B%_B").test()

    actual.assertValue(listOf(
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

    val actual = gateway.searchTagByName("あああ").test()

    actual.assertValue(listOf(
        tagEntity(1, "あああいいいううう", 1),
        tagEntity(3, "いいあああ", 1),
        tagEntity(2, "いいいいいいあああいいいううう", 1)
    ))
  }

  @Test fun updateTagName() {
    val id = gateway.insertTag("before update", 1)
    gateway.updateTagNameById(id, "after update")

    val actual = gateway.selectTagById(id).test()

    actual.assertValue(tagEntity(id, "after update", 1).toOptional())
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

    val afterDelete = gateway.selectTagById(id).test()
    afterDelete.assertValue(None)
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