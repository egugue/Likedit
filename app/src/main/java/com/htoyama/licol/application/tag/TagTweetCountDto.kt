package com.htoyama.licol.application.tag

import com.htoyama.licol.domain.tag.Tag

/**
 * A DTO containing tag and associated tweet count.
 */
data class TagTweetCountDto(
  val tag: Tag,
  val tweetCount: Int
)