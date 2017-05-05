package com.htoyama.likit.data

import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.data.sqlite.tag.TagSqliteDao
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.tag
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TagRepositoryImplTest {
  @Mock lateinit var dao: TagSqliteDao
  @InjectMocks lateinit var repo: TagRepositoryImpl

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

  @Test fun `select all`() {
    val expected = listOf(tag())
    whenever(dao.selectAll(1, 1)).thenReturn(Observable.just(expected))

    repo.findAll(1, 1).test()
        .assertValue(expected)
  }

  @Test fun `not select all if the given page is invalid`() {
    whenever(dao.selectAll(any(), any())).thenReturn(Observable.just(emptyList()))

    try {
      repo.findAll(0, 1)
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("0 < page required but it was 0")
    }

    repo.findAll(1, 1)
  }

  @Test fun `not select all if the given per page is invalid`() {
    whenever(dao.selectAll(any(), any())).thenReturn(Observable.just(emptyList()))

    try {
      repo.findAll(1, 0)
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("1 <= perPage <= 200 required but it was 0")
    }

    repo.findAll(1, 1)
    repo.findAll(1, 200)

    try {
      repo.findAll(1, 201)
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("1 <= perPage <= 200 required but it was 201")
    }
  }

  @Test fun `store new tag`() {
    val newTag = tag(id = Tag.UNASSIGNED_ID)
    val newlyAssignedId = 1L
    whenever(dao.insert(newTag)).thenReturn(newlyAssignedId)

    repo.store(newTag).test()
        .assertValue(newlyAssignedId)
  }

  @Test fun `store tag as update`() {
    val alreadyAssignedId = 1L
    val updating = tag(id = alreadyAssignedId)

    repo.store(updating).test()
        .assertValue(alreadyAssignedId)
  }

  @Test fun `find by name containing the given arg`() {
    val arg = "part of name"
    val expected = listOf(tag())
    whenever(dao.searchTagBy(arg)).thenReturn(Observable.just(expected))

    repo.findByNameContaining(arg).test()
        .assertValue(expected)
  }

  @Test fun `delete a tag with the given id if success`() {
    repo.removeById(1L).test()
        .assertComplete()
  }

  @Test fun `not delete a tag with the given id if failed`() {
    val error = RuntimeException()
    whenever(dao.deleteById(1L)).thenThrow(error)

    repo.removeById(1L).test()
        .assertError(error)
  }
}