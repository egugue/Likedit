package com.egugue.licol.data.sqlite.likedtweet

import com.egugue.licol.common.AllOpen
import com.egugue.licol.data.sqlite.IdMap
import com.egugue.licol.data.sqlite.lib.transaction
import com.egugue.licol.data.sqlite.relation.TweetTagRelation
import com.egugue.licol.data.sqlite.relation.TweetTagRelationTableGateway
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tag.Tag
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

/**
 * A Data Access Object that handles liked Tweets.
 */
@AllOpen
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
   * Select some [LikedTweet]s by the given args
   *
   * @param tagId the identifier of [Tag]
   * @param page
   *     the number of page, which must be a positive integer
   * @param perPage
   *    the number of tweets to retrieve per a page,
   *    which must be between 1 and 200
   */
  fun selectByTagId(tagId: Long, page: Int, perPage: Int): Observable<List<LikedTweet>> {
    val relationList = relationGateway.selectRelationsByTagIdList(listOf(tagId))
    val tweetEntityListAndIdMap = relationList
        .flatMap(
            { likedTweetGateway.selectByTweetIdList(it.map { it.tweetId }, page, perPage) },
            { relList, tweetEntityList -> tweetEntityList to IdMap.basedOnTweetId(relList) })

    return tweetEntityListAndIdMap
        .map { (tweetEntityList, idMap) ->
          tweetEntityList.map { it.toLikedTweet(idMap) }
        }
  }

  /**
   * Select some [LikedTweet]s by the given args
   *
   * @param userId the identifier of [User]
   * @param page
   *     the number of page, which must be a positive integer
   * @param perPage
   *    the number of tweets to retrieve per a page,
   *    which must be between 1 and 200
   */
  fun selectByUserId(userId: Long, page: Int, perPage: Int): Observable<List<LikedTweet>> {
    val tweetEntityList = likedTweetGateway.selectByUserId(userId, page, perPage)
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
   * Select some [LikedTweetEntity]s by the given args
   *
   * @param partOfText the part of text
   */
  fun selectByTextContaining(partOfText: String, page: Int,
      perPage: Int): Observable<List<LikedTweet>> {
    val tweetEntityList = likedTweetGateway.searchByTextContaining(partOfText, page, perPage)
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
   * Insert or update the given [LikedTweet]
   */
  fun insertOrUpdate(tweet: LikedTweet) {
    insertOrUpdate(listOf(tweet))
  }

  /**
   * Insert or update the given list of [LikedTweet]
   */
  fun insertOrUpdate(list: List<LikedTweet>) {
    val entityList = ArrayList<FullLikedTweetEntity>(list.size)
    val relationList = ArrayList<TweetTagRelation>(list.size)

    list.forEach {
      entityList.add(FullLikedTweetEntity.fromLikedTweet(it))

      val tweetId = it.id
      val intermediate = it.tagIdList.map { TweetTagRelation(tweetId, it) }
      relationList.addAll(intermediate)
    }

    briteDatabase.transaction {
      likedTweetGateway.insertOrIgnoreTweetList(entityList)
      relationGateway.insertTweetTagRelation(relationList)
    }
  }

  /**
   * Delete the given of [LikedTweet]
   */
  fun delete(tweetIdList: List<Long>) {
    likedTweetGateway.deleteTweetByIdList(tweetIdList)
  }

  private fun selectRelationsByTweetEntityList(
      tweetList: List<FullLikedTweetEntity>): Observable<List<TweetTagRelation>> {
    if (tweetList.isEmpty()) {
      return Observable.just(emptyList())
    }

    val idList = tweetList.map { it.tweet.id }
    return relationGateway.selectRelationsByTweetIdList(idList)
  }
}
