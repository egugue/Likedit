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