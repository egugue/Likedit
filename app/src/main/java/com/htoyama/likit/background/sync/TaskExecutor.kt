package com.htoyama.likit.background.sync

import com.htoyama.likit.common.Irrelevant
import com.htoyama.likit.data.prefs.AppSetting
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * A class which executes all tasks related to likes sync
 */
class TaskExecutor @Inject constructor(
    private val likedPullTask: LikesPullTask,
    private val nonLikesRemoveTask: NonLikesRemoveTask,
    private val appSetting: AppSetting
) {

  fun execute(): Disposable {
    return likedPullTask.execute()
        .zipWith(nonLikesRemoveTask.execute(), BiFunction<Any, Any, Any> { _, _ -> Irrelevant.get() })
        .subscribeOn(Schedulers.newThread())
        .subscribe(
            { appSetting.setLastSyncedDateAsNow() },
            { } // TODO
        )
  }
}