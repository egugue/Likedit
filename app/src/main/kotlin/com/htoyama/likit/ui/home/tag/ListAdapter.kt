package com.htoyama.likit.ui.home.tag

import android.content.res.Resources
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

internal class ListAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var itemList: List<TagTweetCountDto> = ArrayList()

  fun setItemList(itemList: List<TagTweetCountDto>) {
    this.itemList = itemList
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int = itemList.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_tag, parent, false)
    return ItemViewHolder(view, listener)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val dto = itemList[position]
    (holder as ItemViewHolder).bind(dto.tag, dto.tweetCount)
  }

  class ItemViewHolder(
      itemView: View,
      val listener: OnItemClickListener
  ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val tagNameTv: TextView by bindView(R.id.list_item_tag_name)
    val tweetCountTv: TextView by bindView(R.id.list_item_tweet_count)
    val res: Resources
    lateinit var tag: Tag

    init {
      res = itemView.context.resources
      itemView.setOnClickListener(this)
    }

    fun bind(tag: Tag, tweetCount: Int) {
      this.tag = tag
      tagNameTv.text = tag.name
      tweetCountTv.text = res.getString(R.string.home_tag_related_tweet_count, tweetCount)
    }

    override fun onClick(v: View?) {
      listener.onClickItem(tag)
    }

  }

  interface OnItemClickListener {
    fun onClickItem(tag: Tag)
  }

}