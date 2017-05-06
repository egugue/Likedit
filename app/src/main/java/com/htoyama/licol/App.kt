package com.htoyama.licol

import android.app.Application
import android.content.Context
import com.htoyama.licol.background.sync.TweetSyncService
import com.jakewharton.threetenabp.AndroidThreeTen
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import io.fabric.sdk.android.Fabric

open class App :Application() {

  companion object {
    /**
     * Retrieves the [AppComponent]
     */
    @JvmStatic
    fun component(context: Context): AppComponent = get(context).component

    /**
     * Retrieves the [App]
     */
    fun get(context: Context): App = context.applicationContext as App
  }

  lateinit var component: AppComponent

  override fun onCreate() {
    super.onCreate()
    buildComponent()
    buildFabric()
    AndroidThreeTen.init(this)

    TweetSyncService.scheduleJob(this)
  }

  private fun buildComponent() {
    component = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()
  }

  private fun buildFabric() {
    val c = TwitterAuthConfig(
        BuildConfig.TWITTER_CONSUMER_KEY,
        BuildConfig.TWITTER_CONSUMER_SECRET)
    Fabric.with(this, Twitter(c))
  }
}
