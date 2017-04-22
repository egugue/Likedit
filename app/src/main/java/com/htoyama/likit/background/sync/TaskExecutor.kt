package com.htoyama.likit.background.sync

import com.htoyama.likit.data.prefs.AppSetting
import javax.inject.Inject

/**
 * A class which executes all tasks related to likes sync
 */
class TaskExecutor @Inject constructor(
    private val likedPullTask: LikesPullTask
    /*
    private val nonLikesRemoveTask: NonLikesRemoveTask,
    private val appSetting: AppSetting
    */
){

  fun execute() {
    /*
    Thread {
      val t1 = likedPullTask.execute()
      val t2 = nonLikesRemoveTask.execute()

      if (t1 != null || t2 !== null) {
        appSetting.setLastSyncedDateAsNow()
      }
    }
    */
  }
}