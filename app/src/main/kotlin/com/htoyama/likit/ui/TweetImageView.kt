package com.htoyama.likit.ui

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

/**
 * A [AppCompatImageView] which show an image in appropriate sizes.
 *
 * When using this view, we must set minWidth and maxHeight.
 */
class TweetImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {
  private var aspectRatio: Double = 1.0
  private var imageWidth: Int = 0
  private var imageHeight: Int = 0

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    if (imageHeight > imageWidth && imageHeight > maxHeight) {
      val width = minimumWidth
      val height = maxHeight
      setMeasuredDimension(width, height)
    } else {
      val width = measuredWidth
      val height = Math.round(measuredWidth / aspectRatio).toInt()
      setMeasuredDimension(width, height)
    }
  }

  fun setImageSize(width: Int, height: Int) {
    this.aspectRatio = width.toDouble() / height
    imageWidth = width
    imageHeight = height
  }

}
