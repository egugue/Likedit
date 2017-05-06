package com.htoyama.licol

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import butterknife.bindView
import com.htoyama.licol.data.prefs.AppSetting
import com.htoyama.licol.domain.likedtweet.LikedTweet
import com.htoyama.licol.domain.likedtweet.LikedTweetRepository
import com.htoyama.licol.domain.tag.TagRepository
import com.htoyama.licol.ui.common.tweet.OnTweetClickListener
import com.htoyama.licol.ui.TweetAdapter
import com.htoyama.licol.ui.auth.AuthActivity
import com.htoyama.licol.ui.home.HomeActivity
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.twitter.sdk.android.core.TwitterCore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : RxAppCompatActivity() {

  @Inject lateinit var likedRepository: LikedTweetRepository
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
        .subscribe(
            { likedList ->
              adapter.setTweetList(likedList.map(LikedTweet::tweet))
            }//,
            //TODO
            //Throwable::printStackTrace
        )

    initLastSyncedTimeText()
  }

  @Inject lateinit var appSetting: AppSetting

  private fun initLastSyncedTimeText() {
    val view = findViewById(R.id.last_sycned_time) as TextView
    appSetting.getLastSyncedDate()
        .compose(bindToLifecycle())
        .subscribe { view.text = it.toString() }
  }
}
