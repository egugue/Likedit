package com.egugue.licol.ui.home.user.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.egugue.licol.R
import com.egugue.licol.domain.user.User
import com.squareup.picasso.Picasso

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
        .into(h.avatarView)

    h.nameView.text = user.name
    h.screenNameView.text = user.screenName
    h.likedTweetCountView.text = "${user.likedTweetIdList.size} likes"
  }

  override fun onViewDetachedFromWindow(holder: Holder?) {
    super.onViewDetachedFromWindow(holder)
  }

  class Holder : EpoxyHolder() {
    lateinit var itemView: View
    lateinit var avatarView: ImageView
    lateinit var nameView: TextView
    lateinit var screenNameView: TextView
    lateinit var likedTweetCountView: TextView

    override fun bindView(itemView: View) {
      this.itemView = itemView
      //TODO: use ButterKnife or something like it
      avatarView = itemView.findViewById(R.id.user_avatar) as ImageView
      nameView = itemView.findViewById(R.id.user_name) as TextView
      screenNameView = itemView.findViewById(R.id.user_screen_name) as TextView
      likedTweetCountView = itemView.findViewById(R.id.user_liked_tweet_count) as TextView
    }
  }

}
