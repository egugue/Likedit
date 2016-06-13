package com.htoyama.likit.data.pref

import android.content.SharedPreferences
import twitter4j.auth.AccessToken
import javax.inject.Inject

class AccessTokenPrefsDao
  @Inject constructor(val pref: SharedPreferences) {

  fun store(accessToken: AccessToken) {
    pref.edit()
        .putString("token", accessToken.token)
        .putString("secret", accessToken.tokenSecret)
        .apply()
  }

  fun get(): AccessToken? {
    val token = pref.getString("token", null)
    val secret = pref.getString("secret", null)
    if (token == null || secret == null) {
      return null
    }
    return AccessToken(token, secret)
  }

}

