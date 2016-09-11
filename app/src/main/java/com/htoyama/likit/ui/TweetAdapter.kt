package com.htoyama.likit.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htoyama.likit.R
import com.htoyama.likit.domain.tweet.Tweet
import com.htoyama.likit.ui.common.tweet.OnTweetClickListener
import com.htoyama.likit.ui.common.tweet.TweetView
import java.util.ArrayList

/**
 * Created by toyamaosamuyu on 2016/06/17.
 */
class TweetAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private lateinit var tweetList: List<Tweet>
  var listener: OnTweetClickListener? = null

  init {
    this.tweetList = ArrayList<Tweet>()
  }

  fun setTweetList(tweetList: List<Tweet>) {
    this.tweetList = tweetList
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val childView = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_tweet, parent, false)

    return TweetHolder(childView)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as TweetHolder).bind(tweetList[position], listener)
  }

  override fun getItemCount(): Int {
    return tweetList.size
  }

  private class TweetHolder constructor(itemView: View)
      : RecyclerView.ViewHolder(itemView) {

    fun bind(tweet: Tweet, listener: OnTweetClickListener?) {
      (itemView as TweetView).apply {
        this.listener = listener
        setTweet(tweet)
      }
    }

  }
}
