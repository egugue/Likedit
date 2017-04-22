package com.htoyama.likit

import android.app.Application
import android.app.Service
import android.content.Context
import com.htoyama.likit.background.sync.TweetSyncService
import com.htoyama.likit.old.AppComponent
import com.htoyama.likit.old.AppModule
import com.jakewharton.threetenabp.AndroidThreeTen
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasDispatchingServiceInjector
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

open class App :Application(), HasDispatchingServiceInjector{

  companion object {
    /**
     * Retrieves the [AppComponent]
     */
    @JvmStatic
    @Deprecated("use commponent builded by Dagger2.10")
    fun oldComponent(context: Context): AppComponent = get(context).component

    /**
     * Retrieves the [App]
     */
    fun get(context: Context): App = context.applicationContext as App
  }

  lateinit var component: AppComponent
  var realm: Realm? = null
  @Inject lateinit var serviceInjecter: DispatchingAndroidInjector<Service>
  //@Inject lateinit var helper: SqliteOpenHelper

  override fun onCreate() {
    super.onCreate()
    buildComponent()
    buildFabric()
    buildRealm()
    AndroidThreeTen.init(this)

    TweetSyncService.scheduleJob(this)
  }

  override fun onTerminate() {
    super.onTerminate()
    realm?.close()
  }

  override fun serviceInjector(): DispatchingAndroidInjector<Service> = serviceInjecter

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
    component = com.htoyama.likit.old.DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()

    DaggerAppComponent.builder()
        .application(this)
        .build()
        .inject(this)
  }

  private fun buildFabric() {
    val c = TwitterAuthConfig(
        BuildConfig.TWITTER_CONSUMER_KEY,
        BuildConfig.TWITTER_CONSUMER_SECRET);
    Fabric.with(this, Twitter(c));
  }

}
