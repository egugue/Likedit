package com.htoyama.likit.data.sqlite.tag

import com.htoyama.likit.data.sqlite.IdMap
import com.htoyama.likit.data.sqlite.relation.TweetTagRelation
import com.htoyama.likit.data.sqlite.relation.TweetTagRelationTableGateway
import com.htoyama.likit.domain.tag.Tag
import com.squareup.sqlbrite.BriteDatabase
import io.reactivex.Observable
import javax.inject.Inject

class TagSqliteDao @Inject constructor(
    private val briteDatabase: BriteDatabase,
    private val tagGateway: TagTableGateway,
    private val relationGateway: TweetTagRelationTableGateway
){

  fun searchTagBy(name: String): Observable<List<Tag>> {
    val tagEntityList = tagGateway.searchTagByName(name)
    val tweetEntityListAndIdMap = tagEntityList
        .flatMap(
            { selectRelationsBy(it) },
            { entityList, relations -> entityList to IdMap.basedOnTagId(relations) })

    return tweetEntityListAndIdMap
        .map { (tagEntityList, idMap) ->
          tagEntityList.map { it.toTag(idMap) }
        }
  }

  private fun selectRelationsBy(tagEntityList: List<TagEntity>): Observable<List<TweetTagRelation>> {
    if (tagEntityList.isEmpty()) {
      return Observable.just(emptyList())
    }

    val idList = tagEntityList.map { it.id }
    return relationGateway.selectRelationsByTagIdList(idList)
  }
}