package com.egugue.licol.data.sqlite.user

import com.egugue.licol.data.sqlite.userSqliteDao
import com.egugue.licol.testutil.SqliteTestingRule
import com.egugue.licol.user
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserSqliteDaoTest {
  @Rule @JvmField val rule = SqliteTestingRule()

  lateinit var userDao: UserSqliteDao

  @Before fun setUp() {
    userDao = userSqliteDao(rule.briteDB)
  }

  @Test fun `select all users`() {
    val perPage = 2
    val expected1 = (1L..2L).map { user(id = it, name = it.toString()) }
    val expected2 = (3L..4L).map { user(id = it, name = it.toString()) }
    val expected3 = listOf(user(id = 5, name = "5"))

    (expected1 + expected2 + expected3).forEach { userDao.insertOrUpdate(it) }

    // order by name
    userDao.selectAll(1, perPage).test().assertValue(expected1)
    userDao.selectAll(2, perPage).test().assertValue(expected2)
    userDao.selectAll(3, perPage).test().assertValue(expected3)
    userDao.selectAll(4, perPage).test().assertValue(emptyList())
    userDao.selectAll(100, perPage).test().assertValue(emptyList())
  }

  @Test fun `select by id list`() {
    (1L..10L).forEach { userDao.insertOrUpdate(user(id = it)) }

    val expected = userDao.selectByIdList(listOf(1L, 2L, 10L, 99L)).blockingFirst()

    assertThat(expected).containsExactly(
        user(1L),
        user(2L),
        user(10L)
    )
  }

  @Test fun `search by name or screen name`() {
    // given
    val searchWord = "B%_B"
    val expected1 = user(name = "bb%_bbbbaacc", id = 1)
    val expected2 = user(name = "zzz", screenName = "baaccb%_b", id = 2)
    val expected3 = user(name = "aaaB%_Bccc", screenName = "aaaB%_Bccc", id = 3)

    val unexpecteds = listOf(
        user(name = "aaabbbccc", id = 4),
        user(name = "aaaBBBccc", id = 5),
        user(name = "abc", id = 6),
        user(name = "aabbc", id = 7),
        user(name = "あああいいいううう", id = 8))

    (listOf(expected1, expected2, expected3) + unexpecteds)
        .forEach { userDao.insertOrUpdate(it) }

    // order by name
    userDao.searchByNameOrScreenName(searchWord, searchWord, 200).test()
        .assertValue(listOf(expected3, expected1, expected2))
  }

  @Test fun `search by name or screen name if multi byte is given`() {
    // given
    val searchWord = "あああ"
    val expected1 = user(name = "あああいいいううう", id = 1)
    val expected2 = user(name = "んんん", screenName = "いいいいいいあああいいいううう", id = 2)
    val expected3 = user(name = "いいあああ", screenName = "いいあああ", id = 3)

    val unexpecteds = listOf(
        user(name = "いいああ", id = 4),
        user(name = "ああいいうう", id = 5),
        user(name = "あｓｆｄｓｆｄｆｓ", id = 6))

    (listOf(expected1, expected2, expected3) + unexpecteds)
        .forEach { userDao.insertOrUpdate(it) }

    // order by name
    userDao.searchByNameOrScreenName(searchWord, searchWord, 200).test()
        .assertValue(listOf(expected1, expected3, expected2))
  }
}