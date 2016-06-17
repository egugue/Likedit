package com.htoyama.likit.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.htoyama.likit.R
import com.squareup.picasso.Picasso

import com.twitter.sdk.android.core.models.Tweet

/**
 * Created by toyamaosamuyu on 2016/06/17.
 */
class TweetView
    @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

  private var tweet: Tweet? = null

  val avatorIv: ImageView by bindView(R.id.tw__tweet_author_avatar)
  val fullNameTv: TextView by bindView(R.id.tw__tweet_author_full_name)
  val screenNameTv: TextView by bindView(R.id.tw__tweet_author_screen_name)
  val contentTv: TextView by bindView(R.id.tw__tweet_text)
  val timestampTv: TextView by bindView(R.id.tw__tweet_timestamp)

  fun setTweet(tweet: Tweet) {
    this.tweet = tweet
    render()
  }

  private fun render() {
    val tweet = displayTweet() ?: return

    Picasso.with(context)
        .load(avatorUrl(tweet.user.profileImageUrlHttps))
        .into(avatorIv)

    fullNameTv.text = tweet.user.name
    screenNameTv.text = tweet.user.screenName
    contentTv.text = tweet.text

  }

  private fun avatorUrl(avatorUrl: String): String {
    return avatorUrl.replace("_normal", "_reasonably_small")
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
  }

  private fun displayTweet(): Tweet? {
    if (tweet == null || (tweet as Tweet).retweetedStatus == null) {
      return tweet
    } else {
      return (tweet as Tweet).retweetedStatus
    }
  }

}
