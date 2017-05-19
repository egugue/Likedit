package com.egugue.licol.ui.tag.tweet.select

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.egugue.licol.R
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.ui.common.tweet.TweetView
import java.util.*

internal class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var itemList: List<LikedTweet> = Collections.emptyList()

  fun addItemList(itemList: List<LikedTweet>) {
    this.itemList += itemList
    notifyDataSetChanged() //TODO: specify position
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_tweet, parent, false)
    return ItemViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as ItemViewHolder).bind(itemList[position])
  }

  override fun getItemCount() = itemList.size

  class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(liked: LikedTweet) {
      //TODO
      // (itemView as TweetView).setTweet(liked.tweet)
    }

  }

}