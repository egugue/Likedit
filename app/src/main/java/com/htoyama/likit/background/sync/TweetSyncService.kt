package com.htoyama.likit.background.sync

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.SystemClock
import android.app.job.JobScheduler
import android.content.ComponentName
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * A [JobService] which syncs cached liked tweets on user's device
 * with liked tweets on twitter's server.
 */
class TweetSyncService : JobService() {

  override fun onCreate() {
    super.onCreate()
  }

  @SuppressLint("NewApi")
  override fun onStartJob(params: JobParameters?): Boolean {
    Log.d("ーーー", "onStartJob")

    Observable.fromCallable {
      Log.d("ーーーー", "fromCallable")

      SystemClock.sleep(3000)
    }
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            {
              jobFinished(params, false)
              scheduleJob(this@TweetSyncService)
            },
            { t ->
              t.printStackTrace()
              jobFinished(params, false)
              scheduleJob(this@TweetSyncService)
            }
        )
    return true
  }

  override fun onStopJob(params: JobParameters?): Boolean {
    // TODO: unsubscribe observable
    return false
  }

  companion object {
    const val ID: Int = 10000

    @SuppressLint("NewApi")
    fun scheduleJob(context: Context) {
      val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

      val a = jobScheduler.schedule(
          JobInfo.Builder(ID, ComponentName(context, TweetSyncService::class.java))
              .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
              //.setPeriodic(TimeUnit.MINUTES.toMillis(15))
              .setRequiresCharging(true)
              //.setRequiresDeviceIdle(true)
              .build())

      Log.d("ーーー", a.toString())
    }
  }
}