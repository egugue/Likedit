package com.egugue.licol.background.sync

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.app.job.JobScheduler
import android.content.ComponentName
import android.util.Log
import com.egugue.licol.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * A [JobService] which syncs cached liked tweets on user's device
 * with liked tweets on twitter's server.
 */
class TweetSyncService : JobService() {

  @Inject lateinit var taskExecutor: TaskExecutor
  lateinit private var disposable: Disposable

  override fun onCreate() {
    super.onCreate()

    DaggerSyncComponent.builder()
        .appComponent(App.component(this))
        .build()
        .inject(this)
  }

  override fun onStartJob(params: JobParameters?): Boolean {
    Log.d("ーーー", "onStartJob")

    disposable = taskExecutor.execute()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            {
              jobFinished(params, false)
              scheduleJob(applicationContext)
            },
            { e ->
              e.printStackTrace() //TODO: https://github.com/egugue/Likedit/issues/95
              jobFinished(params, false)
              scheduleJob(applicationContext)
            }
        )
    return true
  }

  override fun onStopJob(params: JobParameters?): Boolean {
    if (!disposable.isDisposed) {
      disposable.dispose()
    }
    return true
  }

  companion object {
    const val ID: Int = 10000

    fun scheduleJob(context: Context) {
      val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

      val a = jobScheduler.schedule(
          JobInfo.Builder(ID, ComponentName(context, TweetSyncService::class.java))
              .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
              .setPeriodic(TimeUnit.HOURS.toMillis(3))
              .setRequiresCharging(true)
              .setRequiresDeviceIdle(true)
              .build())

      Log.d("ーーー", a.toString())
    }
  }
}