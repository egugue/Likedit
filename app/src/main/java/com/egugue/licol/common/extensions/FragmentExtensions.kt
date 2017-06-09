package com.egugue.licol.common.extensions

import android.content.Intent
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.customtabs.CustomTabsSession
import android.support.v4.app.Fragment
import android.widget.Toast
import com.egugue.licol.ui.common.customtabs.CustomTabActivityHelper

fun Fragment.toast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

/**
 * Open a link with a given session
 */
fun Fragment.openLink(url: String, session: CustomTabsSession?) {
  val activity = activity?: return
  val customTabIntent = CustomTabsIntent.Builder(session)
      .setShowTitle(true)
      .addDefaultShareMenuItem()
      .build()

  CustomTabActivityHelper.openCustomTab(activity, customTabIntent, Uri.parse(url), { activity, uri ->
    val intent = Intent(Intent.ACTION_VIEW, uri)
    activity.startActivity(intent)
  })

}
