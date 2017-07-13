package com.egugue.licol.data.sqlite.tag

import com.egugue.licol.common.AllOpen
import com.egugue.licol.common.Contract
import com.egugue.licol.data.sqlite.IdMap
import com.egugue.licol.data.sqlite.lib.transaction
import com.egugue.licol.data.sqlite.relation.TweetTagRelation
import com.egugue.licol.data.sqlite.relation.TweetTagRelationTableGateway
import com.egugue.licol.domain.tag.Tag
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Observable
import javax.inject.Inject

@AllOpen
class TagSqliteDao @Inject constructor(
    private val db: BriteDatabase,
    private val tagGateway: TagTableGateway,
    private val relationGateway: TweetTagRelationTableGateway
) {

  /**
   * Select a tag with the given id.
   *
   * @throws RuntimeException if there is no such tag with the given id
   */
  fun selectTagBy(tagId: Long): Observable<Tag> {
    val tagEntityAndIdMap = tagGateway.selectTagById(tagId)
        .flatMap(
            { selectRelationsBy(listOf(it)) },
            { tagEntity, relations -> tagEntity to IdMap.basedOnTagId(relations) })

    return tagEntityAndIdMap
        .map { (tagEntity, idMap) -> tagEntity.toTag(idMap) }
  }

  /**
   * Select All [Tag]s as list by the given args
   */
  fun selectAll(page: Int, perPage: Int): Observable<List<Tag>> {
    val tagEntityList = tagGateway.selectAll(page, perPage)
    val tagEntityListAndIdMap = tagEntityList.flatMap(
        { selectRelationsBy(it) },
        { tagEntityList, relationList -> tagEntityList to IdMap.basedOnTagId(relationList) })

    return tagEntityListAndIdMap
        .map { (tagEntityList, idMap) ->
          tagEntityList.map { it.toTag(idMap) }
        }
  }

  /**
   * Search tags which name contains the given name.
   */
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

  /**
   * Insert a tag
   *
   * @return the newly assigned id
   */
  fun insert(tag: Tag): Long {
    Contract.require(tag.id == Tag.UNASSIGNED_ID, "tag id must be TAG.UNASSIGNED_ID. but was ${tag.id}")

    return db.transaction {
      val newlyAssignedId = tagGateway.insertTag(tag.name, tag.createdAt.time)

      val relationList = tag.tweetIdList.map { TweetTagRelation(it, newlyAssignedId) }
      relationGateway.insertTweetTagRelation(relationList)

      newlyAssignedId
    }
  }

  /**
   * Update the given tag name by the given tag id
   */
  fun updateName(tag: Tag) {
    Contract.require(tag.id != Tag.UNASSIGNED_ID, "tag id must not be TAG.UNASSIGNED_ID")

    db.transaction {
      tagGateway.updateTagNameById(tag.id, tag.name)

      val relationList = tag.tweetIdList.map { TweetTagRelation(it, tag.id) }
      relationGateway.deleteByTweetIdList(listOf(tag.id))
      relationGateway.insertTweetTagRelation(relationList)
    }
  }

  /**
   * Delete a tag with the given tag id
   */
  fun deleteById(tagId: Long) {
    tagGateway.deleteTagById(tagId)
  }

  private fun selectRelationsBy(tagEntityList: List<TagEntity>): Observable<List<TweetTagRelation>> {
    if (tagEntityList.isEmpty()) {
      return Observable.just(emptyList())
    }

    val idList = tagEntityList.map { it.id }
    return relationGateway.selectRelationsByTagIdList(idList)
  }
}