package com.htoyama.likit.background.sync

import com.htoyama.likit.common.AllOpen
import com.htoyama.likit.common.Irrelevant
import com.htoyama.likit.data.sqlite.tweet.TweetTableGateway
import io.reactivex.Single
import javax.inject.Inject

/**
 * A task which removes no longer liked tweet on local.
 */
@AllOpen
class NonLikesRemoveTask @Inject constructor(
    private val tweetTableGateway: TweetTableGateway
) : Task {

  override fun execute(): Single<Any> {
    return Single.just(Irrelevant.get())
  }
}