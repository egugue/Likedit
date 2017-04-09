package com.htoyama.likit

import android.app.Application
import android.content.Context
import com.htoyama.likit.background.sync.TweetSyncService
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration

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
  var realm: Realm? = null

  override fun onCreate() {
    super.onCreate()
    buildComponent()
    buildFabric()
    buildRealm()

    TweetSyncService.scheduleJob(this)
  }

  override fun onTerminate() {
    super.onTerminate()
    realm?.close()
  }

  private fun buildRealm() {
    try {
      Realm.setDefaultConfiguration(
          RealmConfiguration.Builder(this)
              .schemaVersion(1)
              .build())
      realm = Realm.getDefaultInstance()
    } catch(e: UnsatisfiedLinkError) {
      // when using Robolectric, Realm throw this error.
      // So we ignore it only when debug
      if (!BuildConfig.DEBUG) {
        throw e
      }
    }
  }

  private fun buildComponent() {
    component = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()
  }

  private fun buildFabric() {
    val c = TwitterAuthConfig(
        BuildConfig.TWITTER_CONSUMER_KEY,
        BuildConfig.TWITTER_CONSUMER_SECRET);
    Fabric.with(this, Twitter(c));
  }

}
