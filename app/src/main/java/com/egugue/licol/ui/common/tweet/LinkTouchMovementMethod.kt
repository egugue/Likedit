package com.egugue.licol.ui.common.tweet

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.widget.TextView

import com.twitter.sdk.android.tweetui.internal.HighlightedClickableSpan
import android.view.MotionEvent.*

typealias OnTweetTextClickListener = (Unit) -> Unit

/**
 * A movement method that finds some [HighlightedClickableSpan]s in [TextView]
 * and calls [HighlightedClickableSpan.select] when the span is clicked and released.
 */
class LinkTouchMovementMethod constructor(val listener: OnTweetTextClickListener) : LinkMovementMethod() {

  private var highlightedSpan: HighlightedClickableSpan? = null

  override fun onTouchEvent(textView: TextView, spannable: Spannable,
                            event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        highlightedSpan = getSpan(textView, spannable, event)
        if (highlightedSpan != null) {
          highlightLink(spannable)
          return true // consume the event
        }
      }

      ACTION_UP -> {
        if (highlightedSpan != null) {
          // propagate a link clicked event
          highlightedSpan!!.onClick(textView)
          releaseHighlightingLink(spannable)
          return true // consume the event
        } else {
          // propagate the whole text clicked event
          listener.invoke(Unit)
        }
      }

      ACTION_MOVE -> {
        val touchedSpan = getSpan(textView, spannable, event)
        if (highlightedSpan != null && touchedSpan !== highlightedSpan) {
          // release if moving after a link is pushed
          releaseHighlightingLink(spannable)
        }
      }

      ACTION_CANCEL -> {
        if (highlightedSpan != null) {
          releaseHighlightingLink(spannable)
        }
      }
    }

    return false
  }

  private fun highlightLink(spannable: Spannable) {
    highlightedSpan!!.select(true)
    Selection.setSelection(spannable, spannable.getSpanStart(highlightedSpan),
        spannable.getSpanEnd(highlightedSpan))
  }

  private fun releaseHighlightingLink(spannable: Spannable) {
    highlightedSpan!!.select(false)
    highlightedSpan = null
    Selection.removeSelection(spannable)
  }

  private fun getSpan(textView: TextView, spannable: Spannable, event: MotionEvent)
      : HighlightedClickableSpan? {
    var x = event.x.toInt()
    var y = event.y.toInt()
    x -= textView.totalPaddingLeft
    y -= textView.totalPaddingTop
    x += textView.scrollX
    y += textView.scrollY

    val layout = textView.layout
    val line = layout.getLineForVertical(y)
    val off = layout.getOffsetForHorizontal(line, x.toFloat())

    val link = spannable.getSpans(off, off, HighlightedClickableSpan::class.java)
    var span: HighlightedClickableSpan? = null
    if (link.isNotEmpty()) {
      span = link[0]
    }
    return span
  }
}
