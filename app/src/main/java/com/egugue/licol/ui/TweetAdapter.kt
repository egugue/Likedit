package com.egugue.licol.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.egugue.licol.R
import com.egugue.licol.domain.tweet.Tweet
import com.egugue.licol.ui.common.tweet.OnTweetClickListener
import com.egugue.licol.ui.common.tweet.OldTweetView
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
      (itemView as OldTweetView).apply {
        this.listener = listener
        setTweet(tweet)
      }
    }

  }
}
