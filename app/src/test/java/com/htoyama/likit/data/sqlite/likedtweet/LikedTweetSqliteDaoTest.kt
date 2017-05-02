package com.htoyama.likit.data.sqlite.likedtweet

import com.htoyama.likit.data.sqlite.relation.TweetTagRelationTableGateway
import com.htoyama.likit.data.sqlite.tag.TagTableGateway
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

  lateinit var gateway: TweetTagRelationTableGateway
  lateinit var tagGateway: TagTableGateway
  lateinit var tweetGateway: LikedTweetTableGateway
  lateinit var dao: LikedTweetSqliteDao

  @Before fun setUp() {
    gateway = TweetTagRelationTableGateway(rule.briteDB)
    tagGateway = TagTableGateway(rule.briteDB)
    tweetGateway = LikedTweetTableGateway(rule.briteDB)
    dao = LikedTweetSqliteDao(rule.briteDB, tweetGateway, gateway)
  }

  @Ignore("until sqlite dao classes is created")
  @Test fun select() {
    val result = dao.select(1, 2).test()

    // given
    tagGateway.insertTag("1", 1)
    tagGateway.insertTag("2", 2)

    dao.insertOrUpdate(listOf(
        likedTweet(tweet(id = 1), tagIdList = listOf(1, 2)),
        likedTweet(tweet(id = 2), tagIdList = listOf(1)),
        likedTweet(tweet(id = 3), tagIdList = listOf(1))
    ))

    result.assertValue(listOf(likedTweet()))
  }

}