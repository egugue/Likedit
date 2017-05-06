package com.htoyama.licol.domain.tweet.media

import com.twitter.sdk.android.core.models.MediaEntity

/**
 * Created by toyamaosamuyu on 2016/06/28.
 */
data class Video(
    val url: String,
    val sizes: Sizes
) {
  companion object {
    fun from(media: MediaEntity): Video {
      if (!"video".equals(media.type)) {
        //TODO
        throw IllegalArgumentException("")
      }

      val sizeMedium = Size(
          media.sizes.medium.w,
          media.sizes.medium.h)

      return Video(
          url = media.mediaUrlHttps,
          sizes = Sizes(sizeMedium))
    }
  }
}
