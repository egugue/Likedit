package com.egugue.licol.background.sync

import android.support.annotation.VisibleForTesting
import com.egugue.licol.background.SerivceScope
import com.egugue.licol.common.AllOpen
import com.egugue.licol.common.Irrelevant
import com.egugue.licol.common.extensions.isTwitterRateLimitException
import com.egugue.licol.common.extensions.onErrorReturnOrJustThrow
import com.egugue.licol.data.net.FavoriteService
import com.twitter.sdk.android.core.models.Tweet
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

/**
 * A task which fetches likes list on twitter's server and stores the fetched list to SQLite on local.
 */
@SerivceScope
@AllOpen
class LikedTweetPullTask @Inject constructor(
    private val favoriteService: FavoriteService,
    private val dbFacade: SqliteFacade
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
        .doOnNext { insertLikedTweetAndUser(it) }

    return seriesOfTask
        .map { Irrelevant.get() }
        .onErrorReturnOrJustThrow {
          if (it.isTwitterRateLimitException()) Irrelevant.get() else throw it
        }
        .lastOrError()
  }

  private fun insertLikedTweetAndUser(list: List<Tweet>) {
    if (list.isEmpty()) {
      return
    }

    dbFacade.insertLikedTweetAndUser(list)
  }
}