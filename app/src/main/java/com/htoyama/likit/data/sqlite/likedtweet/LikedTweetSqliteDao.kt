package com.htoyama.likit.data.sqlite.likedtweet

import com.htoyama.likit.common.Contract
import com.htoyama.likit.data.sqlite.IdMap
import com.htoyama.likit.data.sqlite.lib.transaction
import com.htoyama.likit.data.sqlite.relation.TweetTagRelation
import com.htoyama.likit.data.sqlite.relation.TweetTagRelationTableGateway
import com.htoyama.likit.domain.likedtweet.LikedTweet
import com.squareup.sqlbrite.BriteDatabase
import io.reactivex.Observable
import java.util.ArrayList
import javax.inject.Inject

/**
 * A Data Access Object that handles liked Tweets.
 */
class LikedTweetSqliteDao @Inject constructor(
    private val briteDatabase: BriteDatabase,
    private val likedTweetGateway: LikedTweetTableGateway,
    private val relationGateway: TweetTagRelationTableGateway
) {

  /**
   * Select some [LikedTweet]s by the given page and per page
   *
   * @param page
   *     the number of page, which must be a positive integer
   * @param perPage
   *    the number of tweets to retrieve per a page,
   *    which must be between 1 and 200
   */
  fun select(page: Int, perPage: Int): Observable<List<LikedTweet>> {
    Contract.require(page > 0, "0 < page required but it was $page")
    Contract.require(perPage in 1..200, "0 < perPage < 201 required but it was $perPage")

    val tweetEntityList = likedTweetGateway.selectTweet(page, perPage)
    val tweetEntityListAndIdMap = tweetEntityList
        .flatMap(
            { selectRelationsByTweetEntityList(it) },
            { entityList, relations -> entityList to IdMap.basedOnTweetId(relations) })

    return tweetEntityListAndIdMap
        .map { (tweetEntityList, idMap) ->
          tweetEntityList.map { it.toLikedTweet(idMap) }
        }
  }

  /**
   * Insert or update the given list of [LikedTweet]
   */
  fun insertOrUpdate(list: List<LikedTweet>) {
    val entityList = ArrayList<FullLikedTweetEntity>(list.size)
    val relationList = ArrayList<TweetTagRelation>(list.size)

    list.forEach {
      entityList.add(FullLikedTweetEntity.fromLikedTweet(it))

      val tweetId = it.tweet.id
      val intermediate = it.tagIdList.map { TweetTagRelation(tweetId, it) }
      relationList.addAll(intermediate)
    }

    briteDatabase.transaction {
      likedTweetGateway.insertOrUpdateTweetList(entityList)
      relationGateway.insertTweetTagRelation(relationList)
    }
  }

  /**
   * Delete the given of [LikedTweet]
   */
  fun delete(tweetIdList: List<Long>) {
    likedTweetGateway.deleteTweetByIdList(tweetIdList)
  }

  private fun selectRelationsByTweetEntityList(tweetList: List<FullLikedTweetEntity>): Observable<List<TweetTagRelation>> {
    if (tweetList.isEmpty()) {
      return Observable.just(emptyList())
    }

    val idList = tweetList.map { it.tweet.id }
    return relationGateway.selectRelationsByTweetIdList(idList)
  }
}
