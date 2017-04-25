package com.htoyama.likit.background.sync

import android.support.annotation.VisibleForTesting
import com.htoyama.likit.background.SerivceScope
import com.htoyama.likit.common.AllOpen
import com.htoyama.likit.common.Irrelevant
import com.htoyama.likit.common.extensions.isTwitterRateLimitException
import com.htoyama.likit.common.extensions.onErrorReturnOrJustThrow
import com.htoyama.likit.data.common.net.FavoriteService
import com.htoyama.likit.data.sqlite.likedtweet.LikedTweetTableGateway
import com.twitter.sdk.android.core.models.Tweet
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

/**
 * A task which fetches likes list on twitter's server and stores the fetched list to SQLite on local.
 */
@SerivceScope
@AllOpen
class LikesPullTask @Inject constructor(
    private val favoriteService: FavoriteService,
    private val tweetTableGateway: LikedTweetTableGateway
) : Task {

  @VisibleForTesting
  fun executeForTest() {
    Thread {
      execute().subscribe()
    }.start()
  }

  override fun execute(): Single<Any> {
    val seriesOfTask = Observable.fromIterable(1..4) //TODO: use specific page number
        .flatMap { favoriteService.list(null, null, 200, null, null, true, it).toObservable() }
        .takeUntil { it.isEmpty() }
        .doOnNext { insertOrUpdateLikedList(it) }

    return seriesOfTask
        .map { Irrelevant.get() }
        .onErrorReturnOrJustThrow {
          if (it.isTwitterRateLimitException()) Irrelevant.get() else throw it
        }
        .lastOrError()
  }

  private fun insertOrUpdateLikedList(list: List<Tweet>) {
    if (list.isEmpty()) {
      return
    }
    tweetTableGateway.insertOrUpdateTweetList(
        Mapper.mapToFullTweetEntityList(list)
    )
  }
}