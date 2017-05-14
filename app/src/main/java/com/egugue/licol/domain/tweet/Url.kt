package com.egugue.licol.domain.tweet

import com.twitter.sdk.android.core.models.UrlEntity

/**
 * Created by toyamaosamuyu on 2016/07/01.
 */
data class Url(
    val url: String,
    val displayUrl: String,
    val start: Int,
    val end: Int
) {

  companion object {
    fun from(dto: UrlEntity): Url {
      return Url(
          url = dto.url,
          displayUrl = dto.displayUrl,
          start = dto.start,
          end = dto.end
      )
    }
  }
}