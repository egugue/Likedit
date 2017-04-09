package com.htoyama.likit.background.sync

import com.htoyama.likit.common.extensions.*
import com.htoyama.likit.data.sqlite.tweet.FullTweetEntity
import com.htoyama.likit.data.sqlite.tweet.TweetEntity
import com.htoyama.likit.data.sqlite.user.UserEntity
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User

/**
 * Transform between [FullTweetEntity] and [Tweet]
 */
object Mapper {

  /**
   * Map the given [Tweet] list into [FullTweetEntity] list
   */
  fun mapToFullTweetEntityList(dtoList: List<Tweet>): List<FullTweetEntity> {
    return dtoList.map { it -> mapToFullTweetEntity(it) }
  }

  /**
   * Map the given [Tweet] into [FullTweetEntity]
   */
  fun mapToFullTweetEntity(d: Tweet): FullTweetEntity {
    var dto = d
    if (d.retweetedStatus != null) {
      dto = d.retweetedStatus
    }

    val user = mapIntoUserEntity(dto.user)
    val mediaList = dto.extractAllMedias()
    val photoList = mediaList.extractPhotoList()
    val video = mediaList.extractVideo()
    val text = dto.textForDisplay()
    val urlsList = dto.extractUrlList()
    val createdAt = dto.createdToLong()

    return FullTweetEntity(
        TweetEntity(
            d.id,
            user.id,
            text,
            photoList,
            urlsList,
            video,
            createdAt
        ),
        user
    )
  }

  private fun mapIntoUserEntity(dto: User): UserEntity {
    return UserEntity(
        dto.id,
        dto.name,
        dto.screenName,
        dto.avatarUrl()
    )
  }
}