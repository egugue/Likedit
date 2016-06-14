package com.htoyama.likit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.htoyama.likit.data.pref.AccessTokenPrefsDao
import com.htoyama.likit.ui.TwitterLogger
import com.htoyama.likit.ui.auth.AuthActivity
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.Twitter
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var twitter: Twitter
  @Inject lateinit var dao: AccessTokenPrefsDao

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    App.component(this).inject(this)

    findViewById(R.id.text_view)?.setOnClickListener {
      startActivity(AuthActivity.createIntent(this))
    }

    return
    Observable.fromCallable { twitter.favorites }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Subscriber<ResponseList<Status>>() {

          override fun onCompleted() {
            //throw UnsupportedOperationException()
          }

          override fun onError(e: Throwable?) {
            throw UnsupportedOperationException()
          }

          override fun onNext(t: ResponseList<Status>?) {
            if (t == null) {
              return
            }

            for (status in t) {
              TwitterLogger.log(status)
            }
          }
        })
  }

}
