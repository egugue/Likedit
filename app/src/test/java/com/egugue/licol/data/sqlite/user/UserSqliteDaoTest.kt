package com.egugue.licol.data.sqlite.user

import com.egugue.licol.data.sqlite.likedTweetSqliDao
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetSqliteDao
import com.egugue.licol.data.sqlite.userSqliteDao
import com.egugue.licol.likedTweet
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
  lateinit var tweetDao: LikedTweetSqliteDao

  @Before fun setUp() {
    userDao = userSqliteDao(rule.briteDB)
    tweetDao = likedTweetSqliDao(rule.briteDB)
  }

  @Test fun `select all users`() {
    // given
    val u1 = user(id = 1, likedTweetIdList = listOf(1L, 2L, 3L))
    val u2 = user(id = 2, likedTweetIdList = listOf(4L, 5L))
    val u3 = user(id = 3, likedTweetIdList = listOf(6L))

    listOf(u1, u2, u3).forEach { user ->
      userDao.insertOrUpdate(user)
      user.likedTweetIdList.forEach { tweetDao.insertOrUpdate(likedTweet(id = it, userId = user.id)) }
    }

    // when then
    val perPage = 2
    val a1 = userDao.selectAllOrderedByLikedTweetCount(1, perPage).blockingFirst()
    assertThat(a1).containsExactly(u1, u2)

    val a2 = userDao.selectAllOrderedByLikedTweetCount(2, perPage).blockingFirst()
    assertThat(a2).containsExactly(u3)

    val a3 = userDao.selectAllOrderedByLikedTweetCount(3, perPage).blockingFirst()
    assertThat(a3).isEmpty()

    val a100 = userDao.selectAllOrderedByLikedTweetCount(100, perPage).blockingFirst()
    assertThat(a100).isEmpty()
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

  @Test fun `select user with liked tweet id list`() {
    // given
    userDao.insertOrUpdate(user(id = 1L))

    val tweetDao = likedTweetSqliDao(rule.briteDB)
    (1L..3L).forEach { tweetDao.insertOrUpdate(likedTweet(id = it, userId = 1L)) }

    // when
    val actual = userDao.selectAllOrderedByLikedTweetCount(1, 1).blockingFirst().first()

    // then
    assertThat(actual.likedTweetIdList).containsExactly(
        1L, 2L, 3L
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