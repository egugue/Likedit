package com.htoyama.likit.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.res.ResourcesCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.htoyama.likit.R
import com.htoyama.likit.util.LinkClickListener
import com.htoyama.likit.util.TweetTextLinkifier
import com.htoyama.likit.util.TweetTextUtils
import com.htoyama.toGone
import com.htoyama.toVisible
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
  val mediaIv: TweetImageView by bindView(R.id.tw__tweet_media)

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
    setMedia(tweet)
  }

  private fun setContent(tweet: Tweet?) {
    if (tweet == null) {
      contentTv.text = ""
      contentTv.toGone()
      return
    }

    val formatTweetText = TweetTextUtils.formatTweetText(tweet)
    val linkColor = ResourcesCompat.getColor(resources, R.color.tweet_text_link, null)
    val linkHighLightColor = ResourcesCompat.getColor(resources, R.color.tweet_text_link_highlight, null)
    val hasPhoto = TweetMediaUtils.hasPhoto(tweet)
    val text = TweetTextLinkifier.linkifyUrls(formatTweetText, linkClickListener,
        hasPhoto, linkColor, linkHighLightColor)

    if (text == null) {
      contentTv.text = ""
      contentTv.toGone()
      return
    }

    contentTv.text = text
    contentTv.toVisible()
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
    return if (TextUtils.isEmpty(screenName)) {
      ""
    } else if (screenName[0] == '@') {
      screenName
    } else {
      "@" + screenName
    }
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

  private fun setMedia(tweet: Tweet?) {
    if (tweet == null || !
    TweetMediaUtils.hasPhoto(tweet)) {
      mediaIv.visibility = View.GONE
      return
    }

    val photoEntity = TweetMediaUtils.getPhotoEntity(tweet)
    if (photoEntity != null && photoEntity.sizes != null && photoEntity.sizes.medium != null) {
        mediaIv.setImageSize(photoEntity.sizes.medium.w, photoEntity.sizes.medium.h);
    }
    mediaIv.visibility = View.VISIBLE
    Picasso.with(context)
        .load(photoEntity.mediaUrlHttps)
        .centerCrop()
        .fit()
        .into(mediaIv)
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
