package com.htoyama.likit.background.sync

import com.htoyama.likit.data.sqlite.tweet.TweetTableGateway
import javax.inject.Inject

/**
 * A task which removes no longer liked tweet on local.
 */
class NonLikesRemoveTask @Inject constructor(
    private val tweetTableGateway: TweetTableGateway
) : Task {

  override fun execute(): Throwable? {
    // TODO
    return null
  }
}