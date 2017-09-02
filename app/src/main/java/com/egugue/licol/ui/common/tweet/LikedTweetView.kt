package com.egugue.licol.ui.common.tweet

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.res.ResourcesCompat
import android.text.Spannable
import android.util.AttributeSet
import com.egugue.licol.R
import com.egugue.licol.common.extensions.toGone
import com.egugue.licol.common.extensions.toVisible
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.user.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.liked_tweet_view.view.tweet_photo
import kotlinx.android.synthetic.main.liked_tweet_view.view.tweet_text
import kotlinx.android.synthetic.main.liked_tweet_view.view.tweet_timestamp
import kotlinx.android.synthetic.main.liked_tweet_view.view.user_avatar
import kotlinx.android.synthetic.main.liked_tweet_view.view.user_name
import kotlinx.android.synthetic.main.liked_tweet_view.view.user_screen_name

/**
 * A view showing a tweet user liked.
 */
class LikedTweetView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

  val onLinkClickListener: OnLinkClickListener = { listener?.onLinkClicked(it) }
  var listener: OnItemClickListener? = null

  /**
   * Bind the given args to this view
   */
  fun bindItem(tweet: LikedTweet, user: User) {
    setOnClickListener { listener?.onWholeClicked(tweet, user) }

    renderUser(user)
    renderTimestamp(tweet.createdAt)
    renderText(tweet, user)
    renderPhoto(tweet.photoList)
  }

  private fun renderUser(user: User) {
    user_avatar.setOnClickListener { listener?.onUserAvatarClicked(user) }

    Picasso.with(context)

        .load(user.avatorUrl)
        .into(user_avatar)

    user_name.text = user.name
    user_screen_name.text = "@${user.screenName}"
  }

  private fun renderTimestamp(createdAt: Long) {
    tweet_timestamp.text = TweetDateUtils.getRelativeTimeString(
        resources, System.currentTimeMillis(), createdAt)
  }

  private fun renderText(tweet: LikedTweet, user: User) {
    val linklifier = TweetTextLinklifer()
    tweet_text.text = linklifier.linklifyText(
        tweet.text,
        tweet.urlList,
        ResourcesCompat.getColor(resources, R.color.tweet_action_light_highlight_color,
            context.theme),
        onLinkClickListener
    )

    val method = LinkTouchMovementMethod { listener?.onWholeClicked(tweet, user) }
    tweet_text.setOnTouchListener { v, event ->
      tweet_text.movementMethod = method
      val res = method.onTouchEvent(tweet_text, tweet_text.text as Spannable, event)
      tweet_text.movementMethod = null
      tweet_text.isFocusable = false
      res
    }
  }

  private fun renderPhoto(photoList: List<Photo>) {
    if (photoList.isEmpty()) {
      tweet_photo.toGone()
      return
    }

    for (photo in photoList) {
      tweet_photo.setImageSize(photo.sizes.medium.width, photo.sizes.medium.height)
      tweet_photo.toVisible()

      Picasso.with(context)
          .load(photo.url)
          .into(tweet_photo)

      tweet_photo.setOnClickListener { listener?.onPhotoClicked(photo) }
      break
    }
  }

  /** A listener to be invoked when each item is clicked */
  interface OnItemClickListener {

    /** Invoked when a link is clicked */
    fun onLinkClicked(url: String)

    /** Invoked when the whole view is clicked */
    fun onWholeClicked(likedTweet: LikedTweet, user: User)

    /** Invoked when an user avatar image is clicked */
    fun onUserAvatarClicked(user: User)

    /** Invoked when an photo is clicked */
    fun onPhotoClicked(photo: Photo)
  }
}
