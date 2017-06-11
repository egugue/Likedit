package com.egugue.licol.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.egugue.licol.R
import com.egugue.licol.domain.user.User
import com.squareup.picasso.Picasso

/**
 * An [EpoxyModelWithHolder] which show suggestion of [User]
 */
internal class UserSuggestionEpoxyModel(val user: User)
  : EpoxyModelWithHolder<UserSuggestionEpoxyModel.Holder>() {

  private var listener: View.OnClickListener? = null

  fun setOnItemClickListener(l: View.OnClickListener?): UserSuggestionEpoxyModel {
    listener = l
    return this
  }

  override fun getDefaultLayout(): Int = R.layout.search_user_suggestion_item
  override fun createNewHolder(): Holder = Holder()

  override fun bind(h: Holder) {
    h.itemView.setOnClickListener(listener)

    Picasso.with(h.avatarView.context)
        .load(user.avatorUrl)
        .into(h.avatarView)

    h.nameView.text = user.name
    h.screenNameView.text = "@${user.screenName}"
  }

  class Holder : EpoxyHolder() {
    lateinit var itemView: View

    //@BindView(R.id.user_avatar)
    lateinit var avatarView: ImageView

    //@BindView(R.id.user_name)
    lateinit var nameView: TextView

    //@BindView(R.id.user_screen_name)
    lateinit var screenNameView: TextView

    override fun bindView(itemView: View) {
      this.itemView = itemView
      // ButterKnife.bind(this, itemView)
      avatarView = itemView.findViewById(R.id.user_avatar)
      nameView = itemView.findViewById(R.id.user_name)
      screenNameView = itemView.findViewById(R.id.user_screen_name)
    }
  }
}