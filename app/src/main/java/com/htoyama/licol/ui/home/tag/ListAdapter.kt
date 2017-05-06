package com.htoyama.licol.ui.home.tag

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htoyama.licol.application.tag.TagTweetCountDto
import com.htoyama.licol.databinding.ListItemTagBinding
import com.htoyama.licol.domain.tag.Tag
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
    val binding = ListItemTagBinding.inflate(
        LayoutInflater.from(parent.context), parent, false)
    return ItemViewHolder(binding.root, binding, listener)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val dto = itemList[position]
    (holder as ItemViewHolder).bind(dto)
  }

  class ItemViewHolder(
      itemView: View,
      val binding:ListItemTagBinding,
      val listener: OnItemClickListener
  ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val res: Resources
    lateinit var tag: Tag

    init {
      res = itemView.context.resources
      itemView.setOnClickListener(this)
    }

    fun bind(dto: TagTweetCountDto) {
      this.tag = dto.tag
      binding.dto = dto
    }

    override fun onClick(v: View?) {
      listener.onClickItem(tag)
    }

  }

  interface OnItemClickListener {
    fun onClickItem(tag: Tag)
  }

}