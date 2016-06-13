package com.htoyama.likit

import android.app.Application
import android.content.Context

class App :Application() {

  companion object {
    /**
     * Retrieves the [AppComponent]
     */
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
  }

  private fun buildComponent()  {
    component = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()
  }

}
