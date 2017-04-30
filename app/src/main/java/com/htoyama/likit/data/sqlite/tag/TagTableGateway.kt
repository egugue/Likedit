package com.htoyama.likit.data.sqlite.tag

import com.htoyama.likit.common.None
import com.htoyama.likit.common.Optional
import com.htoyama.likit.common.extensions.toV2Observable
import com.htoyama.likit.common.toOptional
import com.htoyama.likit.data.sqlite.lib.SqliteScripts
import com.htoyama.likit.data.sqlite.lib.createQuery
import com.htoyama.likit.data.sqlite.lib.transaction
import com.squareup.sqlbrite.BriteDatabase
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A gateway that handles data via mainly tag table.
 */
class TagTableGateway @Inject constructor(
    private val db: BriteDatabase
) {

  /**
   * Select the tag with the given id.
   * If there is no such tag, return null.
   */
  fun selectTagById(id: Long): Observable<Optional<TagEntity>> {
    val stmt = TagEntity.FACTORY.select_by_id(id)
    return db.createQuery(stmt)
        .mapToOneOrDefault(
            { TagEntity.FACTORY.select_by_idMapper().map(it).toOptional() },
            None
        )
        .toV2Observable()
  }

  /**
   * Search tags which name contains the given name.
   */
  fun searchTagByName(name: String): Observable<List<TagEntity>> {
    val escaped = name.replace("%", "$%")
        .replace("_", "\$_")

    val stmt = TagEntity.FACTORY.search_by_name(escaped)

    return db.createQuery(stmt)
        .mapToList { TagEntity.FACTORY.search_by_nameMapper().map(it) }
        .toV2Observable()
  }

  /**
   * Insert the given name and created as a Tag.
   *
   * @return the row ID of the last row inserted, if this insert is successful. -i otherwise.
   */
  fun insertTag(name: String, created: Long): Long {
    return db.writableDatabase.use {
      db.transaction {
        SqliteScripts.insertTag(db, name, created)
      }
    }
  }

  /**
   * Update the name of the tag with the given id.
   *
   * @throws IllegalStateException if the tag with the id has not inserted.
   */
  fun updateTagNameById(id: Long, name: String) {
    db.writableDatabase.use {
      db.transaction {
        if (SqliteScripts.selectTagById(it, id) == null) {
          throw IllegalArgumentException("tried to update the name of the tag with id($id). but it has not inserted.")
        }

        SqliteScripts.updateTagNameById(db, name, id)
      }
    }
  }

  /**
   * Delete the tag with the given id.
   *
   * @throws IllegalStateException if there is no such tag
   */
  fun deleteTagById(id: Long) {
    db.writableDatabase.use {
      db.transaction {
        if (SqliteScripts.selectTagById(it, id) == null) {
          throw IllegalStateException("tried to delete the tag with id($id). but there is no such tag.")
        }
        SqliteScripts.deleteTagById(db, id)
      }
    }
  }
}