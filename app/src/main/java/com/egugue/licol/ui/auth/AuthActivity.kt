package com.egugue.licol.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.egugue.licol.App
import com.egugue.licol.R
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import kotlinx.android.synthetic.main.auth_activity.twitter_login_button

class AuthActivity : AppCompatActivity() {

  companion object {
    fun createIntent(context: Context): Intent {
      return Intent(context, AuthActivity::class.java)
    }
  }

  private lateinit var loginButton: TwitterLoginButton

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.auth_activity)

    twitter_login_button.callback = object : Callback<TwitterSession>() {
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
    twitter_login_button.onActivityResult(requestCode, resultCode, data)
  }

}
