package com.htoyama.likit.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htoyama.likit.R

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.BaseTweetView
import com.twitter.sdk.android.tweetui.CompactTweetView

import java.util.ArrayList

/**
 * Created by toyamaosamuyu on 2016/06/17.
 */
class TweetAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private lateinit var tweetList: List<Tweet>

  init {
    this.tweetList = ArrayList<Tweet>()
  }

  fun setTweetList(tweetList: List<Tweet>) {
    this.tweetList = tweetList
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val tweet: Tweet? = null
    //val childView = CompactTweetView(parent.context, tweet)
    val childView = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_tweet, parent, false)

    return TweetHolder(childView)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as TweetHolder).bind(tweetList[position])
  }

  override fun getItemCount(): Int {
    return tweetList.size
  }

  private class TweetHolder constructor(itemView: View)
      : RecyclerView.ViewHolder(itemView) {

    fun bind(tweet: Tweet) {
      (itemView as TweetView).setTweet(tweet)
    }

  }
}
