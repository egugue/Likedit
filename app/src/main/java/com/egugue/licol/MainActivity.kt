package com.egugue.licol

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.egugue.licol.data.prefs.AppSetting
import com.egugue.licol.domain.likedtweet.LikedTweetRepository
import com.egugue.licol.domain.tag.TagRepository
import com.egugue.licol.ui.common.tweet.OnTweetClickListener
import com.egugue.licol.ui.auth.AuthActivity
import com.egugue.licol.ui.home.HomeActivity
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.twitter.sdk.android.core.TwitterCore
import javax.inject.Inject

class MainActivity : RxAppCompatActivity() {

  @Inject lateinit var likedRepository: LikedTweetRepository
  @Inject lateinit var tagRepository: TagRepository

  @BindView(R.id.auth_button) lateinit var authButton: Button
  @BindView(R.id.home_button) lateinit var homeButton: Button

  val listener: OnTweetClickListener = object : OnTweetClickListener {
    override fun onUrlClicked(url: String) {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
      startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    if (TwitterCore.getInstance().sessionManager.activeSession == null) {
      startActivity(AuthActivity.createIntent(this))
      finish()
      return
    }

    App.component(this).inject(this)
    ButterKnife.bind(this)

    authButton.setOnClickListener {
      startActivity(AuthActivity.createIntent(this))
    }

    homeButton.setOnClickListener {
      val intent = Intent(this, HomeActivity::class.java)
      startActivity(intent)
    }

    val listview = findViewById<RecyclerView>(R.id.list)
    listview.layoutManager = LinearLayoutManager(this)
    /*
    val adapter = TweetAdapter()
    adapter.listener = listener
    listview.adapter = adapter

    likedRepository.find(1, 200)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { likedList ->
              // TODO
              //adapter.setTweetList(likedList.map(LikedTweet::tweet))
            }//,
            //TODO
            //Throwable::printStackTrace
        )
        */

    initLastSyncedTimeText()
  }

  @Inject lateinit var appSetting: AppSetting

  private fun initLastSyncedTimeText() {
    val view = findViewById<TextView>(R.id.last_sycned_time)
    appSetting.getLastSyncedDate()
        .compose(bindToLifecycle())
        .subscribe { view.text = it.toString() }
  }
}
