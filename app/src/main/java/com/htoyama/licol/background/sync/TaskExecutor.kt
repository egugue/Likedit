package com.htoyama.licol.background.sync

import com.htoyama.licol.background.SerivceScope
import com.htoyama.licol.common.Irrelevant
import com.htoyama.licol.common.extensions.zipWith
import com.htoyama.licol.data.prefs.AppSetting
import io.reactivex.Single
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