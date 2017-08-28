package com.egugue.licol.ui.home.user.list

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.egugue.licol.R
import com.egugue.licol.domain.user.User
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.home_user_list_item.user_avatar
import kotlinx.android.synthetic.main.home_user_list_item.user_liked_tweet_count
import kotlinx.android.synthetic.main.home_user_list_item.user_name
import kotlinx.android.synthetic.main.home_user_list_item.user_screen_name

/**
 * An epoxy model representing a [User]
 */
class UserModel(
    private val user: User
) : EpoxyModelWithHolder<UserModel.Holder>() {

  private var listener: View.OnClickListener? = null

  fun setOnItemClickListener(l: View.OnClickListener?): UserModel {
    listener = l
    return this
  }

  override fun createNewHolder(): Holder = Holder()
  override fun getDefaultLayout(): Int = R.layout.home_user_list_item

  override fun bind(h: Holder) {
    h.itemView.setOnClickListener { listener?.onClick(it) }

    val context = h.itemView.context
    //TODO: use Glide
    Picasso.with(context)
        .load(user.avatorUrl)
        .into(h.user_avatar)

    h.user_name.text = user.name
    h.user_screen_name.text = user.screenName
    h.user_liked_tweet_count.text = "${user.likedTweetIdList.size} likes"
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
