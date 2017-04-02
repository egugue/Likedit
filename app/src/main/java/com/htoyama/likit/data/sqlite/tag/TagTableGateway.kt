package com.htoyama.likit.data.sqlite.tag

import com.htoyama.likit.data.sqlite.lib.SqliteOpenHelper
import com.htoyama.likit.data.sqlite.lib.SqliteScripts
import com.htoyama.likit.data.sqlite.lib.transaction
import javax.inject.Inject

/**
 * A gateway that handles data via mainly tag table.
 */
class TagTableGateway @Inject constructor(
    private val h: SqliteOpenHelper
) {

  /**
   * Select the tag with the given id.
   * If there is no such tag, return null.
   */
  fun selectTagById(id: Long): TagEntity? {
    return h.readableDatabase.use {
      SqliteScripts.selectTagById(it, id)
    }
  }

  /**
   * Search tags which name contains the given name.
   */
  fun searchTagByName(name: String): List<TagEntity> {
    val escaped = name.replace("%", "$%")
        .replace("_", "\$_")
    return h.readableDatabase.use {
      SqliteScripts.searchTagByName(it, escaped)
    }
  }

  /**
   * Insert the given name and created as a Tag.
   *
   * @return the row ID of the last row inserted, if this insert is successful. -i otherwise.
   */
  fun insertTag(name: String, created: Long): Long =
      h.writableDatabase.use {
        it.transaction {
          SqliteScripts.insertTag(it, name, created)
        }
      }

  /**
   * Update the name of the tag with the given id.
   *
   * @throws IllegalStateException if the tag with the id has not inserted.
   */
  fun updateTagNameById(id: Long, name: String) {
    h.writableDatabase.use {
      it.transaction {
        if (SqliteScripts.selectTagById(it, id) == null) {
          throw IllegalArgumentException("tried to update the name of the tag with id($id). but it has not inserted.")
        }

        SqliteScripts.updateTagNameById(it, name, id)
      }
    }
  }

  /**
   * Delete the tag with the given id.
   *
   * @throws IllegalStateException if there is no such tag
   */
  fun deleteTagById(id: Long) {
    h.writableDatabase.use {
      it.transaction {
        if (SqliteScripts.selectTagById(it, id) == null) {
          throw IllegalStateException("tried to delete the tag with id($id). but there is no such tag.")
        }
        SqliteScripts.deleteTagById(it, id)
      }
    }
  }
}