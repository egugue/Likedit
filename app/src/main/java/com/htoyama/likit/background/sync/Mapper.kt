package com.htoyama.likit.background.sync

import com.htoyama.likit.common.extensions.*
import com.htoyama.likit.data.sqlite.tweet.FullLikedTweetEntity
import com.htoyama.likit.data.sqlite.tweet.LikedTweetEntity
import com.htoyama.likit.data.sqlite.user.UserEntity
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User

/**
 * Transform between [FullLikedTweetEntity] and [Tweet]
 */
object Mapper {

  /**
   * Map the given [Tweet] list into [FullLikedTweetEntity] list
   */
  fun mapToFullTweetEntityList(dtoList: List<Tweet>): List<FullLikedTweetEntity> {
    return dtoList.map { it -> mapToFullTweetEntity(it) }
  }

  /**
   * Map the given [Tweet] into [FullLikedTweetEntity]
   */
  fun mapToFullTweetEntity(d: Tweet): FullLikedTweetEntity {
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

    return FullLikedTweetEntity(
        LikedTweetEntity(
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