package com.egugue.licol.data.sqlite.lib

import android.database.Cursor
import com.egugue.licol.data.sqlite.user.UserEntity
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.QueryObservable
import com.squareup.sqldelight.SqlDelightStatement

/**
 * Escape string for query.
 */
fun String.escapeForQuery()
    = replace("%", "$%")
    .replace("_", "\$_")

/**
 * Convert Pair of page and perPage into Pair of limit and offset
 */
fun Pair<Int, Int>.toLimitAndOffset(): Pair<Long, Long> {
  val page = first
  val perPage = second

  val limit = perPage.toLong()
  val offset = (page - 1) * limit
  return limit to offset
}

/* Cursor extensions */

/**
 * Convert [Cursor] into [UserEntity]
 */
fun Cursor.toUserEntity(): UserEntity = UserEntity.FACTORY.select_by_id_listMapper().map(this)

/**
 * Convert [Cursor] into [T]
 *
 * @return [T] transformed from [Cursor]. It may be null if the Cursor does'nt have a row.
 * @throws IllegalArgumentException throw it if the Cursor has more than 1 row.
 */
fun <T> Cursor.mapToOne(transform: (Cursor) -> T): T? =
    if (count == 1) {
      moveToFirst()
      transform.invoke(this)
    } else if (count == 0) {
      null
    } else {
      throw IllegalStateException("Cursor must have a row. but was " + count)
    }

/* SQLiteDataBase extensions */

/* BriteDatabase extensions */

fun <T> BriteDatabase.transaction(unitOfWork: () -> T): T {
  val transaction = newTransaction()
  try {
    val r = unitOfWork.invoke()
    transaction.markSuccessful()
    return r
  } finally {
    transaction.end()
  }
}

fun BriteDatabase.createQuery(stmt: SqlDelightStatement): QueryObservable
    = createQuery(stmt.tables, stmt.statement, *stmt.args)
