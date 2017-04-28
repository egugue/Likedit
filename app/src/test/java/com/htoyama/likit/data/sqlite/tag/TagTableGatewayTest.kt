package com.htoyama.likit.data.sqlite.tag

import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.common.None
import com.htoyama.likit.common.Optional
import com.htoyama.likit.common.toOptional
import com.htoyama.likit.data.sqlite.briteDatabaseForTest
import com.htoyama.likit.data.sqlite.tagEntity
import com.squareup.sqlbrite.BriteDatabase
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
  lateinit var db: BriteDatabase

  @Before fun setUp() {
    db = briteDatabaseForTest()
    gateway = TagTableGateway(db)
  }

  @After fun tearDown() {
    db.close()
    RuntimeEnvironment.application.deleteDatabase("likedit")
  }

  @Test fun selectTagById_whenTagInserted() {
    val tagId = gateway.insertTag("foo", 1).blockingGet()
    assertThat(tagId).isNotEqualTo(-1)

    val actual = gateway.selectTagById(tagId).test()
    actual.assertValue(tagEntity(tagId, "foo", 1).toOptional())
  }

  @Test fun selectTagById_whenNoTagInserted() {
    val actual = gateway.selectTagById(1).test()
    actual.assertValue(None as Optional<TagEntity>)
  }

  @Test fun searchTagByName() {
    gateway.insertTag("bb%_bbbbaacc", 1).subscribe() // hit
    gateway.insertTag("baaccb%_b", 1).subscribe() // hit
    gateway.insertTag("aaabbbccc", 1).subscribe()
    gateway.insertTag("aaaBBBccc", 1).subscribe()
    gateway.insertTag("aaaB%_Bccc", 1).subscribe() // hit
    gateway.insertTag("abc", 1).subscribe()
    gateway.insertTag("aabbc", 1).subscribe()
    gateway.insertTag("あああいいいううう", 1).subscribe()

    val actual = gateway.searchTagByName("B%_B").test()

    actual.assertValue(listOf(
        tagEntity(5, "aaaB%_Bccc", 1),
        tagEntity(2, "baaccb%_b", 1),
        tagEntity(1, "bb%_bbbbaacc", 1)
    ))
  }

  @Test fun searchTagNameName_multiByte() {
    gateway.insertTag("あああいいいううう", 1).subscribe()
    gateway.insertTag("いいいいいいあああいいいううう", 1).subscribe()
    gateway.insertTag("いいあああ", 1).subscribe()
    gateway.insertTag("いいああ", 1).subscribe()
    gateway.insertTag("ああいいうう", 1).subscribe()
    gateway.insertTag("あｓｆｄｓｆｄｆｓ", 1).subscribe()

    val actual = gateway.searchTagByName("あああ").test()

    actual.assertValue(listOf(
        tagEntity(1, "あああいいいううう", 1),
        tagEntity(3, "いいあああ", 1),
        tagEntity(2, "いいいいいいあああいいいううう", 1)
    ))
  }

  @Test fun updateTagName() {
    val id = gateway.insertTag("before update", 1).blockingGet()
    gateway.updateTagNameById(id, "after update").subscribe()

    val actual = gateway.selectTagById(id).test()

    actual.assertValue(tagEntity(id, "after update", 1).toOptional())
  }

  @Test fun updateTagName_whenInvalidIdSpecified() {
    gateway.updateTagNameById(1, "the tag with the id has not yet inserted").test()
        .assertError(IllegalArgumentException::class.java)
        .assertErrorMessage("tried to update the name of the tag with id(1). but it has not inserted.")
  }

  @Test fun deleteTagById() {
    val id = gateway.insertTag("any", 1).blockingGet()

    val beforeDelete = gateway.selectTagById(id)
    assertThat(beforeDelete).isNotNull()

    gateway.deleteTagById(id).subscribe()

    val afterDelete = gateway.selectTagById(id).test()
    afterDelete.assertValue(None)
  }

  @Test fun deleteTagById_whenInvaildIdSpecified() {
    gateway.deleteTagById(1).test()
        .assertError(IllegalStateException::class.java)
        .assertErrorMessage("tried to delete the tag with id(1). but there is no such tag.")
  }
}