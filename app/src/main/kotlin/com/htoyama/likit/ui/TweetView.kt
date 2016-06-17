package com.htoyama.likit.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

import com.twitter.sdk.android.core.models.Tweet

/**
 * Created by toyamaosamuyu on 2016/06/17.
 */
class TweetView @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
  private var tweet: Tweet? = null

  fun setTweet(tweet: Tweet) {
    this.tweet = tweet
  }

}
