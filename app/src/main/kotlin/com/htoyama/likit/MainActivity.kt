package com.htoyama.likit

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import butterknife.bindView
import com.htoyama.likit.domain.likedtweet.LikedTweet
import com.htoyama.likit.domain.likedtweet.LikedTweetRepository
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tag.TagRepository
import com.htoyama.likit.ui.common.tweet.OnTweetClickListener
import com.htoyama.likit.ui.TweetAdapter
import com.htoyama.likit.ui.auth.AuthActivity
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var likedTweetRepository: LikedTweetRepository
  @Inject lateinit var tagRepository: TagRepository
  val tagEt: EditText by bindView(R.id.tag_name)
  val tagButton: Button by bindView(R.id.tag_post_button)

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

    tagRepository.findAll()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Subscriber<List<Tag>>() {
          override fun onNext(t: List<Tag>) {
            for (tag in t) {
              Log.d("ーーー", tag.toString())
            }
          }

          override fun onError(e: Throwable?) {
            //throw UnsupportedOperationException()
            e?.printStackTrace()
          }

          override fun onCompleted() {
            //throw UnsupportedOperationException()
          }
        })

    tagButton.setOnClickListener {
      val tag = Tag(
          id = tagRepository.publishNextIdentity(),
          name = tagEt.text.toString(),
          createdAt = Date()
      )
      tagRepository.store(tag)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(object : Subscriber<Void>() {
            override fun onError(e: Throwable?) {
              e?.printStackTrace()
            }

            override fun onNext(t: Void?) {
            }

            override fun onCompleted() {
              Toast.makeText(applicationContext, "save completed", Toast.LENGTH_SHORT).show()
            }

          })
    }

    val listview = findViewById(R.id.list) as RecyclerView
    listview.layoutManager = LinearLayoutManager(this)
    val adapter = TweetAdapter()
    adapter.listener = listener
    listview.adapter = adapter

    likedTweetRepository.findAll()
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
            adapter.setTweetList(t.map { it.tweet })
          }

        })
  }

}
