package com.htoyama.likit.background.sync

import com.htoyama.likit.background.SerivceScope
import com.htoyama.likit.common.Irrelevant
import com.htoyama.likit.common.extensions.zipWith
import com.htoyama.likit.data.prefs.AppSetting
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * A class which executes all tasks related to likes sync
 */
@SerivceScope
class TaskExecutor @Inject constructor(
    private val likedPullTask: LikedTweetPullTask,
    private val nonLikesRemoveTask: NonLikesRemoveTask,
    private val appSetting: AppSetting
) {

  fun execute(): Single<Any> {
    return likedPullTask.execute()
        .zipWith(nonLikesRemoveTask.execute(), { _, _ -> Irrelevant.get() })
        .subscribeOn(Schedulers.newThread())
        .doOnSuccess { appSetting.setLastSyncedDateAsNow() }
  }
}