package com.egugue.licol.background.sync

import com.egugue.licol.common.AllOpen
import com.egugue.licol.common.Irrelevant
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetTableGateway
import io.reactivex.Single
import javax.inject.Inject

/**
 * A task which removes no longer liked tweet on local.
 */
@AllOpen
class NonLikesRemoveTask @Inject constructor(
    private val tweetTableGateway: LikedTweetTableGateway
) : Task {

  override fun execute(): Single<Any> {
    return Single.just(Irrelevant.get())
  }
}