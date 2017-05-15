package com.egugue.licol.ui.auth

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.egugue.licol.App
import com.egugue.licol.R
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class AuthActivity : AppCompatActivity() {

  companion object {
    fun createIntent(context: Context): Intent {
      return Intent(context, AuthActivity::class.java)
    }
  }

  private lateinit var loginButton: TwitterLoginButton

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_auth)
    loginButton = findViewById(R.id.twitter_login_button) as TwitterLoginButton

    loginButton.callback = object : Callback<TwitterSession>() {
      override fun success(result: Result<TwitterSession>) {
        val session = result.data
        Toast.makeText(this@AuthActivity,
            "@" + session.userName + "logged in",
            Toast.LENGTH_SHORT).show()
      }

      override fun failure(exception: TwitterException?) {
        Log.d("---", "Login with Twitter failure", exception)
      }
    }

    App.component(this)
        .inject(this)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    loginButton.onActivityResult(requestCode, resultCode, data)
  }

}
