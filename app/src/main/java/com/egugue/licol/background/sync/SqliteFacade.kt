package com.egugue.licol.background.sync

import com.egugue.licol.background.SerivceScope
import com.egugue.licol.common.AllOpen
import com.egugue.licol.data.sqlite.lib.transaction
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetTableGateway
import com.egugue.licol.data.sqlite.user.UserTableGateway
import com.squareup.sqlbrite2.BriteDatabase
import com.twitter.sdk.android.core.models.Tweet
import javax.inject.Inject

@SerivceScope
@AllOpen
class SqliteFacade @Inject constructor(
    private val db: BriteDatabase,
    private val tweetTableGateway: LikedTweetTableGateway,
    private val userTableGateway: UserTableGateway
) {

  fun insertLikedTweetAndUser(list: List<Tweet>) {
    if (list.isEmpty()) {
      return
    }

    db.transaction {
      userTableGateway.insertOrUpdate(
          list.map { Mapper.mapIntoUserEntity(it.user) }
      )

      tweetTableGateway.insertOrIgnoreTweetList(
          Mapper.mapToFullTweetEntityList(list)
      )
    }
  }
}