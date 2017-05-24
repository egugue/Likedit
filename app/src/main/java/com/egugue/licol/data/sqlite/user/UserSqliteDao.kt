package com.egugue.licol.data.sqlite.user

import com.egugue.licol.common.AllOpen
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetIdAndUserId
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetTableGateway
import com.egugue.licol.domain.user.User
import io.reactivex.Observable
import javax.inject.Inject

@AllOpen
class UserSqliteDao @Inject constructor(
    private val userGateway: UserTableGateway,
    private val tweetGateway: LikedTweetTableGateway
) {

  /**
   * Select All [User]s as list ordered by liked tweet count related to each user
   */
  fun selectAllOrderedByLikedTweetCount(page: Int, perPage: Int): Observable<List<User>> {
    return userGateway.selectAll(page, perPage)
        .flatMap(
            { tweetGateway.selectIdByUserIds(it.map { it.id }) },
            this::mapToUserList
        )
  }

  /**
   * Select some [User]s as list by the given id list
   */
  fun selectByIdList(idList: List<Long>): Observable<List<User>> {
    return userGateway.selectByIdList(idList)
        .flatMap(
            { tweetGateway.selectIdByUserIds(it.map { it.id }) },
            this::mapToUserList
        )
  }

  /**
   * Select some users which have name or screen name with part of the given each arg.
   */
  fun searchByNameOrScreenName(name: String, screenName: String, limit: Int): Observable<List<User>> {
    return userGateway.selectByNameOrScreenName(name, screenName, limit)
        .flatMap(
            { tweetGateway.selectIdByUserIds(it.map { it.id }) },
            this::mapToUserList
        )
  }

  fun insertOrUpdate(user: User) {
    userGateway.insertOrUpdate(UserEntity.from(user))
  }

  private fun mapToUserList(userEntityList: List<UserEntity>, relationList: List<LikedTweetIdAndUserId>): List<User> {
    val table = mutableMapOf<Long, MutableList<Long>>()
    relationList.forEach {
      table.putIfAbsent(it.userId, mutableListOf())
      table[it.userId]!!.add(it.likedTweetId)
    }

    return userEntityList.map {
      val likedTweetIdList = table.getOrDefault(it.id, mutableListOf())
      it.toUser(likedTweetIdList)
    }
  }
}