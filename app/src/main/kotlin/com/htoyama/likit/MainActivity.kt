package com.htoyama.likit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.htoyama.likit.data.common.net.FavoriteService
import com.htoyama.likit.ui.TweetAdapter
import com.htoyama.likit.ui.auth.AuthActivity
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.CompactTweetView
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter
import com.twitter.sdk.android.tweetui.UserTimeline
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var service: FavoriteService

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    App.component(this).inject(this)

    findViewById(R.id.text_view)?.setOnClickListener {
      startActivity(AuthActivity.createIntent(this))
    }

    /*
    val listview = findViewById(R.id.list) as ListView
    val userTimeline = UserTimeline.Builder()
        .screenName("LOREM_com")
        .build();

    val adapter = TweetTimelineListAdapter.Builder(this)
        .setTimeline(userTimeline)
        .build();

    listview.adapter = adapter
    */
    val listview = findViewById(R.id.list) as RecyclerView
    listview.layoutManager = LinearLayoutManager(this)
    val adapter = TweetAdapter()
    listview.adapter = adapter

    service.list(null, null, 30, null, null, false)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Subscriber<List<Tweet>>() {

          override fun onNext(t: List<Tweet>?) {
            if (t == null) {
              return
            }

            adapter.setTweetList(t)
            for (tweet in t) {
              Log.d("---", tweet.user.name)
            }
          }

          override fun onError(e: Throwable?) {
            e?.printStackTrace()
          }

          override fun onCompleted() {
          }

        })
  }

}
