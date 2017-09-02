package com.egugue.licol.ui.search.suggestion

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.egugue.licol.R
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.search.suggestion.UserSuggestionEpoxyModel.Holder
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.search_user_suggestion_item.user_avatar
import kotlinx.android.synthetic.main.search_user_suggestion_item.user_name
import kotlinx.android.synthetic.main.search_user_suggestion_item.user_screen_name

/**
 * An [EpoxyModelWithHolder] which show suggestion of [User]
 */
internal class UserSuggestionEpoxyModel(val user: User)
  : EpoxyModelWithHolder<Holder>() {

  private var listener: View.OnClickListener? = null

  fun setOnItemClickListener(l: View.OnClickListener?): UserSuggestionEpoxyModel {
    listener = l
    return this
  }

  override fun getDefaultLayout(): Int = R.layout.search_user_suggestion_item
  override fun createNewHolder(): Holder = Holder()

  override fun bind(h: Holder) {
    h.itemView.setOnClickListener(listener)

    Picasso.with(h.user_avatar.context)
        .load(user.avatorUrl)
        .into(h.user_avatar)

    h.user_name.text = user.name
    h.user_screen_name.text = "@${user.screenName}"
  }

  class Holder : EpoxyHolder(), LayoutContainer {
    override val containerView: View?
      get() = itemView

    lateinit var itemView: View

    override fun bindView(itemView: View) {
      this.itemView = itemView
    }
  }
}