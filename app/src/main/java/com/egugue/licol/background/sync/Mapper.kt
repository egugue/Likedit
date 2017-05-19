package com.egugue.licol.background.sync

import com.egugue.licol.common.extensions.*
import com.egugue.licol.data.sqlite.likedtweet.FullLikedTweetEntity
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetEntity
import com.egugue.licol.data.sqlite.likedtweet.QuotedTweetEntity
import com.egugue.licol.data.sqlite.user.UserEntity
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

    val userId = mapIntoUserEntity(dto.user).id()
    val mediaList = dto.extractAllMedias()
    val photoList = mediaList.extractPhotoList()
    val video = mediaList.extractVideo()
    val text = dto.textForDisplay()
    val urlsList = dto.extractUrlList()
    val createdAt = dto.createdToLong()

    return FullLikedTweetEntity(
        LikedTweetEntity(
            d.id,
            userId,
            text,
            dto.favoriteCount,
            photoList,
            urlsList,
            video,
            dto.quotedStatus?.id,
            createdAt
        ),
        if (dto.quotedStatus == null) null
        else QuotedTweetEntity(
            dto.quotedStatus.id,
            dto.quotedStatus.textForDisplay(),
            dto.quotedStatus.user.id,
            dto.quotedStatus.user.name,
            dto.quotedStatus.user.screenName,
            dto.quotedStatus.user.avatarUrl()
        )
    )
  }

  fun mapIntoUserEntity(dto: User): UserEntity {
    return UserEntity(
        dto.id,
        dto.name,
        dto.screenName,
        dto.avatarUrl()
    )
  }
}