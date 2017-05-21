package com.egugue.licol.ui.common.tweet

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.res.ResourcesCompat
import android.text.Spannable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.egugue.licol.R
import com.egugue.licol.common.extensions.toGone
import com.egugue.licol.common.extensions.toVisible
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.user.User
import com.squareup.picasso.Picasso

/**
 * A view showing a tweet user liked.
 */
class LikedTweetView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

  val userAvatarIv: ImageView by bindView(R.id.user_avatar)
  val userNameTv: TextView by bindView(R.id.user_name)
  val userScreenNameTv: TextView by bindView(R.id.user_screen_name)
  val tweetTextTv: TextView by bindView(R.id.tweet_text)
  val timestampTv: TextView by bindView(R.id.tweet_timestamp)
  val photoIv: TweetImageView by bindView(R.id.tweet_photo)

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
    userAvatarIv.setOnClickListener { listener?.onUserAvatarClicked(user) }

    Picasso.with(context)
        .load(user.avatorUrl)
        .into(userAvatarIv)

    userNameTv.text = user.name
    userScreenNameTv.text = "@${user.screenName}"
  }

  private fun renderTimestamp(createdAt: Long) {
    timestampTv.text = TweetDateUtils.getRelativeTimeString(
        resources, System.currentTimeMillis(), createdAt)
  }

  private fun renderText(tweet: LikedTweet, user: User) {
    val linklifier = TweetTextLinklifer()
    tweetTextTv.text = linklifier.linklifyText(
        tweet.text,
        tweet.urlList,
        ResourcesCompat.getColor(resources, R.color.tweet_action_light_highlight_color, context.theme),
        onLinkClickListener
    )

    val method = LinkTouchMovementMethod { listener?.onWholeClicked(tweet, user) }
    tweetTextTv.setOnTouchListener { v, event ->
      tweetTextTv.movementMethod = method
      val res = method.onTouchEvent(tweetTextTv, tweetTextTv.text as Spannable, event)
      tweetTextTv.movementMethod = null
      tweetTextTv.isFocusable = false
      res
    }
  }

  private fun renderPhoto(photoList: List<Photo>) {
    if (photoList.isEmpty()) {
      photoIv.toGone()
      return
    }

    for (photo in photoList) {
      photoIv.setImageSize(photo.sizes.medium.width, photo.sizes.medium.height)
      photoIv.toVisible()

      Picasso.with(context)
          .load(photo.url)
          .into(photoIv)

      photoIv.setOnClickListener { listener?.onPhotoClicked(photo) }
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
