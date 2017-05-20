package com.egugue.licol.ui.common.tweet

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.egugue.licol.R
import com.egugue.licol.domain.tweet.Tweet
import com.egugue.licol.common.extensions.toGone
import com.egugue.licol.common.extensions.toVisible
import com.squareup.picasso.Picasso

/**
 * A view displaying [Tweet]
 */
class OldTweetView
    @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

  private var tweet: Tweet? = null
  var listener: OnTweetClickListener? = null

  val avatorIv: ImageView by bindView(R.id.tw__tweet_author_avatar)
  val fullNameTv: TextView by bindView(R.id.tw__tweet_author_full_name)
  val screenNameTv: TextView by bindView(R.id.tw__tweet_author_screen_name)
  val contentTv: TextView by bindView(R.id.tw__tweet_text)
  val timestampTv: TextView by bindView(R.id.tw__tweet_timestamp)
  val mediaIv: TweetImageView by bindView(R.id.tw__tweet_media)

  fun setTweet(tweet: Tweet) {
    this.tweet = tweet
    render()
  }

  private fun render() {
    tweet?: return
    val tweet: Tweet = tweet as Tweet

    Picasso.with(context)
        .load(tweet.user.avatorUrl)
        .into(avatorIv)

    fullNameTv.text = tweet.user.name
    screenNameTv.text = tweet.user.screenName
    timestampTv.text = TweetDateUtils.getRelativeTimeString(
        resources, System.currentTimeMillis(), tweet.createdAt)

    val linklifier = TweetTextLinklifer()
    contentTv.text = linklifier.linklifyText(
        tweet,
        context.resources
            .getColor(R.color.tweet_action_light_highlight_color, context.theme),
        listener)
    contentTv.movementMethod = LinkTouchMovementMethod.instance

    if (tweet.photoList.isEmpty()) {
      mediaIv.toGone()
      return
    }

    for (photo in tweet.photoList) {
      mediaIv.setImageSize(photo.sizes.medium.width, photo.sizes.medium.height);
      mediaIv.toVisible()
      Picasso.with(context)
          .load(photo.url)
          .into(mediaIv)
      break
    }
  }

}
