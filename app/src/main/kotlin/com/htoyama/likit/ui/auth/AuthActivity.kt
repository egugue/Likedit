package com.htoyama.likit.ui.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.htoyama.likit.R
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken

class AuthActivity : AppCompatActivity() {

  companion object {
    fun createIntent(context: Context): Intent {
      return Intent(context, AuthActivity::class.java)
    }
  }

  private lateinit var requestToken: RequestToken

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_auth)
    executeOauth()
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)

    Log.d("---", "onNewIntent")
    storeAccessToken(intent!!)
  }

  private fun executeOauth() {
    val twitter = TwitterFactory.getSingleton();
    val key = getString(R.string.twitter_secret_key)
    val sec = getString(R.string.twitter_secret_token)

    twitter.setOAuthConsumer(key, sec)

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

            //TODO
            Log.d("---", accessToken.toString())
          }

        })
  }

}
