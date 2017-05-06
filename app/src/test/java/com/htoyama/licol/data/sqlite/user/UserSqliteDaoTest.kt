package com.htoyama.licol.data.sqlite.user

import com.htoyama.licol.data.sqlite.likedTweetSqliDao
import com.htoyama.licol.data.sqlite.likedtweet.LikedTweetSqliteDao
import com.htoyama.licol.data.sqlite.userSqliteDao
import com.htoyama.licol.domain.user.User
import com.htoyama.licol.likedTweet
import com.htoyama.licol.testutil.SqliteTestingRule
import com.htoyama.licol.tweet
import com.htoyama.licol.user
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserSqliteDaoTest {
  @Rule @JvmField val rule = SqliteTestingRule()

  lateinit var tweetDao: LikedTweetSqliteDao
  lateinit var userDao: UserSqliteDao

  @Before fun setUp() {
    tweetDao = likedTweetSqliDao(rule.briteDB)
    userDao = userSqliteDao(rule.briteDB)
  }

  @Test fun `select all users`() {
    val perPage = 2
    val expected1 = (1L..2L).map { user(id = it, name = it.toString()) }
    val expected2 = (3L..4L).map { user(id = it, name = it.toString()) }
    val expected3 = listOf(user(id = 5, name = "5"))

    (expected1 + expected2 + expected3).forEach { insertUser(it) }

    // order by name
    userDao.selectAll(1, perPage).test().assertValue(expected1)
    userDao.selectAll(2, perPage).test().assertValue(expected2)
    userDao.selectAll(3, perPage).test().assertValue(expected3)
    userDao.selectAll(4, perPage).test().assertValue(emptyList())
    userDao.selectAll(100, perPage).test().assertValue(emptyList())
  }

  @Test fun `search by name or screen name`() {
    // given
    val searchWord = "B%_B"
    val expected1 = user(name = "bb%_bbbbaacc", id = 1)
    val expected2 = user(name ="zzz", screenName = "baaccb%_b", id = 2)
    val expected3 = user(name = "aaaB%_Bccc", screenName = "aaaB%_Bccc", id = 3)

    val unexpecteds = listOf(
        user(name = "aaabbbccc", id = 4),
        user(name = "aaaBBBccc", id = 5),
        user(name = "abc", id = 6),
        user(name = "aabbc", id = 7),
        user(name = "あああいいいううう", id = 8))

    (listOf(expected1, expected2, expected3) + unexpecteds)
        .forEach { insertUser(it) }

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
        .forEach { insertUser(it) }

    // order by name
    userDao.searchByNameOrScreenName(searchWord, searchWord, 200).test()
        .assertValue(listOf(expected1, expected3, expected2))
  }

  var tweetId = 0L
  private fun insertUser(user: User) {
    tweetDao.insertOrUpdate(likedTweet(tweet = tweet(id = tweetId, user = user)))
    tweetId++
  }
}