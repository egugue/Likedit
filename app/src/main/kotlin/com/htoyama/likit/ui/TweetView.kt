package com.htoyama.likit.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.res.ResourcesCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.htoyama.likit.R
import com.htoyama.likit.util.LinkClickListener
import com.htoyama.likit.util.TweetTextLinkifier
import com.htoyama.likit.util.TweetTextUtils
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.models.MediaEntity

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.internal.SpanClickHandler
import com.twitter.sdk.android.tweetui.internal.TweetMediaUtils

/**
 * Created by toyamaosamuyu on 2016/06/17.
 */
class TweetView
    @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

  private var tweet: Tweet? = null

  val avatorIv: ImageView by bindView(R.id.tw__tweet_author_avatar)
  val fullNameTv: TextView by bindView(R.id.tw__tweet_author_full_name)
  val screenNameTv: TextView by bindView(R.id.tw__tweet_author_screen_name)
  val contentTv: TextView by bindView(R.id.tw__tweet_text)
  val timestampTv: TextView by bindView(R.id.tw__tweet_timestamp)

  fun setTweet(tweet: Tweet) {
    this.tweet = tweet
    render()
  }

  private fun render() {
    val tweet = displayTweet() ?: return

    Picasso.with(context)
        .load(avatorUrl(tweet.user.profileImageUrlHttps))
        .into(avatorIv)

    setFullName(tweet)
    setScreenName(tweet)
    setContent(tweet)
    setTimeStamp(tweet)
  }

  private fun setContent(tweet: Tweet?) {
    if (tweet == null) {
      contentTv.text = ""
      return
    }

    val formatTweetText = TweetTextUtils.formatTweetText(tweet)
    val actionColor = ResourcesCompat.getColor(resources, R.color.tw__tweet_action_color, null)
    val actionHighLightColor = ResourcesCompat.getColor(resources, R.color.tw__tweet_action_light_highlight_color, null)
    val hasPhoto = TweetMediaUtils.hasPhoto(tweet)
    val text = TweetTextLinkifier.linkifyUrls(formatTweetText, linkClickListener,
        hasPhoto, actionColor, actionHighLightColor)

    contentTv.text = text
    SpanClickHandler.enableClicksOnSpans(contentTv)
  }

  private fun setFullName(tweet: Tweet?) {
    if (tweet != null && tweet.user != null && tweet.user.name != null) {
      fullNameTv.text = tweet.user.name
    } else {
      fullNameTv.text = ""
    }
  }

  private fun setScreenName(tweet: Tweet?) {
    if (tweet != null && tweet.user != null && tweet.user.screenName!= null) {
      screenNameTv.text = formatScreenName(tweet.user.screenName)
    } else {
      screenNameTv.text = ""
    }
  }

  private fun formatScreenName(screenName: CharSequence): CharSequence {
    if (TextUtils.isEmpty(screenName)) {
      return "";
    }

    if (screenName.get(0) == '@') {
      return screenName;
    }
    return "@" + screenName;
  }

  private fun setTimeStamp(tweet: Tweet) {
    val createdAt = tweet.createdAt
    val timeStamp: String
    if (createdAt != null && TweetDateUtils.isValidTimestamp(createdAt)) {
      timeStamp = TweetDateUtils.getRelativeTimeString(
          resources, System.currentTimeMillis(), TweetDateUtils.apiTimeToLong(createdAt))
    } else {
      timeStamp = ""
    }

    timestampTv.text = timeStamp
  }

  private fun avatorUrl(avatorUrl: String): String {
    return avatorUrl.replace("_normal", "_reasonably_small")
  }

  private fun displayTweet(): Tweet? {
    if (tweet == null || (tweet as Tweet).retweetedStatus == null) {
      return tweet
    } else {
      return (tweet as Tweet).retweetedStatus
    }
  }

  private val linkClickListener: LinkClickListener = object : LinkClickListener {
    override fun onUrlClicked(url: String?) {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
      context.startActivity(intent)
    }

    override fun onPhotoClicked(mediaEntity: MediaEntity?) {
    }

  }

}
