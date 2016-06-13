package com.htoyama.likit.ui.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.htoyama.likit.App
import com.htoyama.likit.R
import com.htoyama.likit.data.pref.AccessTokenPrefsDao
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken
import javax.inject.Inject

class AuthActivity : AppCompatActivity() {

  companion object {
    fun createIntent(context: Context): Intent {
      return Intent(context, AuthActivity::class.java)
    }
  }

  @Inject lateinit var dao: AccessTokenPrefsDao
  @Inject lateinit var twitter: Twitter
  private lateinit var requestToken: RequestToken

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_auth)

    App.component(this)
        .inject(this)

    executeOauth()
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)

    Log.d("---", "onNewIntent")
    storeAccessToken(intent!!)
  }

  private fun executeOauth() {
    Observable.fromCallable {
        requestToken = twitter.getOAuthRequestToken("htoyama://twitter")
        requestToken.authenticationURL
    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Subscriber<String>(){
          override fun onCompleted() {}

          override fun onError(e: Throwable?) {
            throw UnsupportedOperationException()
          }

          override fun onNext(url: String?) {
            if (url == null) {
              //TODO
              Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
              return
            }

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
          }
        })
  }

  private fun storeAccessToken(intent: Intent) {
    val verifer = intent.data.getQueryParameter("oauth_verifier");
    val twitter = TwitterFactory.getSingleton();
    Observable.fromCallable {
      twitter.getOAuthAccessToken(requestToken, verifer)
    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Subscriber<AccessToken>() {

          override fun onCompleted() {}

          override fun onError(e: Throwable?) {
            throw UnsupportedOperationException()
          }

          override fun onNext(accessToken: AccessToken?) {
            if (accessToken == null) {
              return
            }

            Log.d("---", accessToken.toString())
            dao.store(accessToken)
            twitter.oAuthAccessToken = accessToken
            finish()
          }

        })
  }

}
