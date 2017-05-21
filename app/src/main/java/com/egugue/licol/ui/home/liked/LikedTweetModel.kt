package com.egugue.licol.ui.home.liked

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.egugue.licol.R
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.common.tweet.LikedTweetView

/**
 * An [EpoxyModel] which has [LikedTweet] and [User]
 */
class LikedTweetModel(
    private val likedTweet: LikedTweet,
    private val user: User
) : EpoxyModelWithHolder<LikedTweetModel.Holder>() {

  private var itemClickListener: LikedTweetView.OnItemClickListener? = null

  fun setOnItemClickListener(l: LikedTweetView.OnItemClickListener?): LikedTweetModel {
    itemClickListener = l
    return this
  }

  override fun getDefaultLayout(): Int = R.layout.liked_tweet_view
  override fun createNewHolder(): Holder = Holder()

  override fun bind(holder: Holder) {
    holder.tweetView.bindItem(likedTweet, user)
    holder.tweetView.listener = itemClickListener
  }

  class Holder : EpoxyHolder() {
    lateinit var tweetView: LikedTweetView

    override fun bindView(itemView: View) {
      tweetView = itemView as LikedTweetView
    }
  }
}