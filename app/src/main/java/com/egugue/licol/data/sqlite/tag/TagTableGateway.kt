package com.egugue.licol.data.sqlite.tag

import android.annotation.SuppressLint
import com.egugue.licol.common.None
import com.egugue.licol.common.toOptional
import com.egugue.licol.data.sqlite.lib.*
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A gateway that handles data via mainly tag table.
 */
@SuppressLint("CheckResult")
class TagTableGateway @Inject constructor(
    private val db: BriteDatabase
) {

  /**
   * Select the tag with the given id.
   *
   * @throws RuntimeException if there is no such tag with the given id
   */
  fun selectTagById(id: Long): Observable<TagEntity> {
    val stmt = TagEntity.FACTORY.select_by_id(id)
    return db.createQuery(stmt)
        .mapToOneOrDefault(
            { TagEntity.FACTORY.select_by_idMapper().map(it).toOptional() },
            None
        )
        .doOnNext { if (it is None) throw RuntimeException("No such tag which has id($id)") }
        .map { it.get() }
  }

  fun selectAll(page: Int, perPage: Int): Observable<List<TagEntity>> {
    val (limit, offset) = (page to perPage).toLimitAndOffset()

    val stmt = TagEntity.FACTORY.select_all(limit, offset)
    return db.createQuery(stmt)
        .mapToList { TagEntity.FACTORY.select_allMapper().map(it) }
  }

  /**
   * Search tags which name contains the given name.
   */
  fun searchTagByName(name: String): Observable<List<TagEntity>> {
    val escaped = name.escapeForQuery()

    val stmt = TagEntity.FACTORY.search_by_name(escaped)

    return db.createQuery(stmt)
        .mapToList { TagEntity.FACTORY.search_by_nameMapper().map(it) }
  }

  /**
   * Insert the given name and created as a Tag.
   *
   * @return the row ID of the last row inserted, if this insert is successful. -i otherwise.
   */
  fun insertTag(name: String, created: Long): Long {
    return db.transaction {
      SqliteScripts.insertTag(db, name, created)
    }
  }

  /**
   * Update the name of the tag with the given id.
   *
   * @throws IllegalStateException if the tag with the id has not inserted.
   */
  fun updateTagNameById(id: Long, name: String) {
    if (SqliteScripts.selectTagById(db, id) == null) {
      throw IllegalArgumentException("tried to update the name of the tag with id($id). but it has not inserted.")
    }
    db.transaction {
      SqliteScripts.updateTagNameById(db, name, id)
    }
  }

  /**
   * Delete the tag with the given id.
   *
   * @throws IllegalStateException if there is no such tag
   */
  fun deleteTagById(id: Long) {
    if (SqliteScripts.selectTagById(db, id) == null) {
      throw IllegalStateException("tried to delete the tag with id($id). but there is no such tag.")
    }
    db.transaction {
      SqliteScripts.deleteTagById(db, id)
    }
  }
}