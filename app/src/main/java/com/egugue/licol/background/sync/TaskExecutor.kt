package com.egugue.licol.background.sync

import com.egugue.licol.background.SerivceScope
import com.egugue.licol.common.Irrelevant
import com.egugue.licol.common.extensions.zipWith
import com.egugue.licol.data.prefs.AppSetting
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