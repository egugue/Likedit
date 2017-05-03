package com.htoyama.likit.data.sqlite.likedtweet

import com.htoyama.likit.data.sqlite.likedTweetSqliDao
import com.htoyama.likit.data.sqlite.tag.TagSqliteDao
import com.htoyama.likit.data.sqlite.tagSqliteDao
import com.htoyama.likit.likedTweet
import com.htoyama.likit.testutil.SqliteTestingRule
import com.htoyama.likit.tweet
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LikedTweetSqliteDaoTest {
  @Rule @JvmField val rule = SqliteTestingRule()

  lateinit var tweetDao: LikedTweetSqliteDao
  lateinit var tagDao: TagSqliteDao

  @Before fun setUp() {
    tweetDao = likedTweetSqliDao(rule.briteDB)
    tagDao = tagSqliteDao(rule.briteDB)
  }

  @Ignore("until sqlite dao classes is created")
  @Test fun select() {
  }

}