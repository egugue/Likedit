package com.egugue.licol.common.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.customtabs.CustomTabsIntent
import android.support.customtabs.CustomTabsSession
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.egugue.licol.ui.common.customtabs.CustomTabActivityHelper


fun Activity.toast(message: String)
    = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

@ColorInt
fun Activity.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)

/**
 * Open a link with a given session
 */
fun Activity.openLink(url: String, session: CustomTabsSession?) {
  val customTabIntent = CustomTabsIntent.Builder(session)
      .setShowTitle(true)
      .addDefaultShareMenuItem()
      .build()

  CustomTabActivityHelper.openCustomTab(this, customTabIntent, Uri.parse(url), { activity, uri ->
    val intent = Intent(Intent.ACTION_VIEW, uri)
    activity.startActivity(intent)
  })
}

