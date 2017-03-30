package com.htoyama.likit.data.sqlite.lib

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/* Cursor extensions */

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

/**
 * Convert [Cursor] into a list with given transform.
 * If cursor has no row, then return emtpy list.
 */
fun <T> Cursor.mapToList(transform: (Cursor) -> T): List<T> {
  if (count == 0) {
    return emptyList()
  }

  val list = ArrayList<T>(count)
  val pos = position

  moveToFirst()
  do {
    list.add(transform.invoke(this))
  } while (moveToNext())

  moveToPosition(pos)
  return list
}


/* SQLiteDataBase extensions */

/**
 * This method begins transaction, run the given arg, and end transaction.
 */
fun <T> SQLiteDatabase.transaction(unitOfWork: () -> T): T {
  beginTransaction()
  try {
    val r = unitOfWork.invoke()
    setTransactionSuccessful()
    return r
  } finally {
    endTransaction()
  }
}
