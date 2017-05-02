package com.htoyama.likit.data.sqlite

import com.htoyama.likit.data.sqlite.relation.TweetTagRelation

/**
 * A map which contains tweet and tag identifier
 */
class IdMap private constructor(
    private val map: Map<Long, MutableList<Long>>
) {

  /**
   * Get a value by the given key.
   *
   * If [IdMap] is created by [basedOnTweetId], a returned value is list of tag id.
   */
  fun getOrEmptyList(key: Long): List<Long> {
    return map.getOrDefault(key, mutableListOf()).toList()
  }

  companion object {

    /**
     * Create [IdMap] based on tweet id, which means
     * key is tweet id,
     * value is list of tag id.
     */
    fun basedOnTweetId(list: List<TweetTagRelation>): IdMap {
      val map = mutableMapOf<Long, MutableList<Long>>()
      list.forEach {
        map.putIfAbsent(it.tweetId, mutableListOf())
        map[it.tweetId]!!.add(it.tagId)
      }

      return IdMap(map.toMap())
    }
  }
}
