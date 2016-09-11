package com.htoyama.likit

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import butterknife.bindView
import com.htoyama.likit.domain.liked.LikedTweet
import com.htoyama.likit.domain.liked.LikedRepository
import com.htoyama.likit.domain.tag.TagRepository
import com.htoyama.likit.ui.common.tweet.OnTweetClickListener
import com.htoyama.likit.ui.TweetAdapter
import com.htoyama.likit.ui.auth.AuthActivity
import com.htoyama.likit.ui.home.HomeActivity
import com.twitter.sdk.android.core.TwitterCore
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var likedRepository: LikedRepository
  @Inject lateinit var tagRepository: TagRepository
  val authButton: Button by bindView(R.id.auth_button)
  val homeButton: Button by bindView(R.id.home_button)

  val listener: OnTweetClickListener = object : OnTweetClickListener {
    override fun onUrlClicked(url: String) {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
      startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    if (TwitterCore.getInstance().sessionManager.activeSession == null) {
      startActivity(AuthActivity.createIntent(this))
      finish()
      return
    }

    App.component(this).inject(this)

    authButton.setOnClickListener {
      startActivity(AuthActivity.createIntent(this))
    }

    homeButton.setOnClickListener {
      val intent = Intent(this, HomeActivity::class.java)
      startActivity(intent)
    }

    val listview = findViewById(R.id.list) as RecyclerView
    listview.layoutManager = LinearLayoutManager(this)
    val adapter = TweetAdapter()
    adapter.listener = listener
    listview.adapter = adapter

    likedRepository.find(1, 200)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Subscriber<List<LikedTweet>>() {
          override fun onCompleted() {
            //throw UnsupportedOperationException()
          }

          override fun onError(e: Throwable?) {
            e?.printStackTrace()
          }

          override fun onNext(t: List<LikedTweet>) {
            Log.d("ーーーー", " aaa " + t.size)
            adapter.setTweetList(t.map { it.tweet })
          }

        })
  }

}
