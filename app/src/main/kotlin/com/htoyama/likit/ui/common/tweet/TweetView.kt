package com.htoyama.likit.ui.common.tweet

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.htoyama.likit.R
import com.htoyama.likit.model.tweet.Tweet
import com.htoyama.likit.ui.common.tweet.TweetDateUtils
import com.htoyama.toGone
import com.htoyama.toVisible
import com.squareup.picasso.Picasso

/**
 * Created by toyamaosamuyu on 2016/06/17.
 */
class TweetView
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
    contentTv.text = linklifier.linklifyText(tweet,
        context.getColor(R.color.tweet_action_light_highlight_color), listener)
    contentTv.movementMethod = LinkTouchMovementMethod.instance

    if (tweet.photos.isEmpty()) {
      mediaIv.toGone()
      return
    }

    for (photo in tweet.photos) {
      mediaIv.setImageSize(photo.sizes.medium.width, photo.sizes.medium.height);
      mediaIv.toVisible()
      Picasso.with(context)
          .load(photo.url)
          .into(mediaIv)
      break
    }
  }

}
