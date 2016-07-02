package com.htoyama.likit

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.htoyama.likit.data.common.net.FavoriteService
import com.htoyama.likit.model.tweet.Tweet
import com.htoyama.likit.model.tweet.TweetFactory
import com.htoyama.likit.ui.OnTweetClickListener
import com.htoyama.likit.ui.TweetAdapter
import com.htoyama.likit.ui.auth.AuthActivity
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var tweetFactory: TweetFactory
  @Inject lateinit var service: FavoriteService
  val listener: OnTweetClickListener = object : OnTweetClickListener {
    override fun onUrlClicked(url: String) {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
      startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    App.component(this).inject(this)

    findViewById(R.id.text_view)?.setOnClickListener {
      startActivity(AuthActivity.createIntent(this))
    }

    val listview = findViewById(R.id.list) as RecyclerView
    listview.layoutManager = LinearLayoutManager(this)
    val adapter = TweetAdapter()
    adapter.listener = listener
    listview.adapter = adapter

    service.list(null, null, 30, null, null, true)
        .map { tweetFactory.createListFrom(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Subscriber<List<Tweet>>() {

          override fun onNext(t: List<Tweet>?) {
            if (t == null) {
              return
            }

            adapter.setTweetList(t)
          }

          override fun onError(e: Throwable?) {
            e?.printStackTrace()
          }

          override fun onCompleted() {
          }

        })
  }

}
