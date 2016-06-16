package com.htoyama.likit.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.BaseTweetView
import com.twitter.sdk.android.tweetui.CompactTweetView

import java.util.ArrayList

/**
 * Created by toyamaosamuyu on 2016/06/17.
 */
class TweetAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var tweetList: List<Tweet>? = null

  init {
    this.tweetList = ArrayList<Tweet>()
  }

  fun setTweetList(tweetList: List<Tweet>) {
    this.tweetList = tweetList
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val tweet: Tweet? = null
    val childView = CompactTweetView(parent.context, tweet)

    return TweetHolder(childView)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as TweetHolder).bind(tweetList!![position])
  }

  override fun getItemCount(): Int {
    return tweetList!!.size
  }

  private class TweetHolder constructor(itemView: View)
      : RecyclerView.ViewHolder(itemView) {

    fun bind(tweet: Tweet) {
      (itemView as BaseTweetView).tweet = tweet
    }

  }
}
