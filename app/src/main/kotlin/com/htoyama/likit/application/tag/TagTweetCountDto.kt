package com.htoyama.likit.application.tag

import com.htoyama.likit.domain.tag.Tag

/**
 * A DTO containing tag and associated tweet count.
 */
data class TagTweetCountDto(
  val tag: Tag,
  val tweetCount: Int
)