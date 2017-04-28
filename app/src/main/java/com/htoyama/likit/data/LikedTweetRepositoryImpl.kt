package com.htoyama.likit.data

import com.htoyama.likit.domain.likedtweet.LikedTweet
import com.htoyama.likit.domain.likedtweet.LikedTweetRepository
import com.htoyama.likit.domain.tag.Tag
import io.reactivex.Single

class LikedTweetRepositoryImpl: LikedTweetRepository {

  override fun findByTag(tag: Tag): Single<List<Tag>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun find(page: Int, count: Int): Single<List<LikedTweet>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}