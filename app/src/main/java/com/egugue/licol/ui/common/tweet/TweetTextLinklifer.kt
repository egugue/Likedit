package com.egugue.licol.ui.common.tweet

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.View
import com.egugue.licol.domain.tweet.Url

typealias OnLinkClickListener = (String) -> Unit

class TweetTextLinklifer {

  fun linklifyText(text: String, urlList: List<Url>, bgColor: Int, listener: OnLinkClickListener): CharSequence {
    val ssb = SpannableStringBuilder(text)
    linklifyUrl(urlList, bgColor, listener, ssb)
    return ssb
  }

  private fun linklifyUrl(urlList: List<Url>, bgColor: Int, listener: OnLinkClickListener,
                          ssb: SpannableStringBuilder) {
    var offset = 0
    var start: Int
    var end: Int
    var urlLength: Int
    for (url in urlList) {
      urlLength = url.url.length
      start = url.start + offset
      end = start + url.displayUrl.length

      val span = createClickableLinkSpan(url.url, bgColor, listener)
      ssb.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

      offset = url.displayUrl.length - urlLength
    }
  }

  private fun createClickableLinkSpan(url: String, bgColor: Int,
      listener: OnLinkClickListener): ClickableLinkSpan {
    return object : ClickableLinkSpan(bgColor) {
      override fun onClick(widget: View?) {
        listener.invoke(url)
      }
    }
  }

}