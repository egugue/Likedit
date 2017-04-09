package com.htoyama.likit.background.sync

import android.support.annotation.VisibleForTesting
import com.htoyama.likit.data.common.net.FavoriteService
import com.htoyama.likit.data.sqlite.tweet.TweetTableGateway
import com.twitter.sdk.android.core.models.Tweet
import javax.inject.Inject

/**
 * A task which fetches likes list on twitter's server and stores the fetched list.
 */
class LikesPullTask @Inject constructor(
    private val favoriteService: FavoriteService,
    private val tweetTableGateway: TweetTableGateway
) {

  @VisibleForTesting
  fun executeForTest() {
    Thread {
      execute()
    }.start()
  }

  /**
   * Execute this task
   */
  fun execute() {
    try {
      //TODO: retrieve user's liked tweet count from twitter's server
      (1..4).forEach {
        val likedList = fetchLikedList(it)
        if (likedList.isEmpty()) {
          return
        }

        insertOrUpdateLikedList(likedList)
      }
    } catch (e: Exception) {
      //TODO
      e.printStackTrace()
    }
  }

  private fun fetchLikedList(page: Int): List<Tweet> {
    return favoriteService.list(null, null, 200, null, null, true, page)
        .blockingGet()
  }

  private fun insertOrUpdateLikedList(list: List<Tweet>) {
    tweetTableGateway.insertOrUpdateTweetList(
        Mapper.mapToFullTweetEntityList(list)
    )
  }
}