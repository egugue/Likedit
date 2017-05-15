package com.egugue.licol.common.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import com.google.firebase.analytics.FirebaseAnalytics.Param
import com.google.firebase.analytics.FirebaseAnalytics.Event
import timber.log.Timber
import javax.inject.Singleton

/**
 * A facade executing analytics event
 */
@Singleton
class Analytics @Inject constructor(
    private val analytics: FirebaseAnalytics
){

  /**
   * Deploy a view log event
   */
  fun viewEvent(e: ViewEvent) {
    val b = Bundle()
    val event = e.toString().toLowerCase()
    b.putString(Param.ITEM_NAME, e.toString().toLowerCase())
    analytics.logEvent(Event.VIEW_ITEM, b)

    Timber.i("analytics open screen: $event")
  }

}