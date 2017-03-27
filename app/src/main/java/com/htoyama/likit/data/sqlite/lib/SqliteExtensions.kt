package com.htoyama.likit.data.sqlite.lib

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/* Cursor extensions */

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
fun SQLiteDatabase.transaction(unitOfWork: () -> Unit) {
  beginTransaction()
  try {
    unitOfWork.invoke()
    setTransactionSuccessful()
  } finally {
    endTransaction()
  }
}
