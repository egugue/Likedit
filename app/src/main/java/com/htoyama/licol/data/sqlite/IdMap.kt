package com.htoyama.licol.data.sqlite

import com.htoyama.licol.data.sqlite.relation.TweetTagRelation

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

    /**
     * Create [IdMap] based on tag id, which means
     * key is tag id,
     * value is list of tweet id.
     */
    fun basedOnTagId(list: List<TweetTagRelation>): IdMap {
      val map = mutableMapOf<Long, MutableList<Long>>()
      list.forEach { (value, key) ->
        map.putIfAbsent(key, mutableListOf())
        map[key]!!.add(value)
      }

      return IdMap(map.toMap())
    }
  }
}
