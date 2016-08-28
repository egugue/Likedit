package com.htoyama.likit.ui.home.tag

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.htoyama.likit.R
import com.htoyama.likit.application.tag.TagTweetCountDto
import com.htoyama.likit.domain.tag.Tag
import java.util.*

internal class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var itemList: List<TagTweetCountDto> = ArrayList()

  fun setItemList(itemList: List<TagTweetCountDto>) {
    this.itemList = itemList
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int = itemList.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_tag, parent, false)
    return ItemViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val dto = itemList[position]
    (holder as ItemViewHolder).bind(dto.tag, dto.tweetCount)
  }

  class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tagNameTv: TextView by bindView(R.id.list_item_tag_name)
    val tweetCountTv: TextView by bindView(R.id.list_item_tweet_count)

    fun bind(tag: Tag, tweetCount: Int) {
      tagNameTv.text = tag.name
      tweetCountTv.text = tweetCount.toString()
    }
  }

}