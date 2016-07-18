package com.htoyama.likit.ui.home.tag

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.htoyama.likit.R
import com.htoyama.likit.domain.tag.Tag
import java.util.*

internal class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var itemList: List<Tag> = ArrayList()

  fun setItemList(itemList: List<Tag>) {
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
    (holder as ItemViewHolder).bind(itemList[position])
  }

  class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tagNameTv: TextView by bindView(R.id.list_item_tag_name)

    fun bind(tag: Tag) {
      tagNameTv.text = tag.name
    }

  }

}