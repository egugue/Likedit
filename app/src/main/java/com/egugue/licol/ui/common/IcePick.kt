package com.egugue.licol.ui.common

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @see http://qiita.com/boohbah/items/268aaefbee9b3595a99e
 */
interface IcePick {
  val bundle: Bundle

  fun <T> state(initialValue: T): ReadWriteProperty<IcePick, T> = State(initialValue)

  fun saveInstanceState(outState: Bundle) {
    outState.putAll(bundle)
  }

  fun restoreInstanceState(savedInstanceState: Bundle?) {
    savedInstanceState ?: return

    bundle.putAll(savedInstanceState)
    savedInstanceState.keySet()
        .filter { !it.startsWith("icepick_") }
        .forEach {
          bundle.remove(it)
        }
  }

  private class State<T> constructor(private val initialValue: T) : ReadWriteProperty<IcePick, T> {

    override fun getValue(thisRef: IcePick, property: KProperty<*>): T {
      val key = "icepick_${property.name}"
      val bundle = thisRef.bundle

      @Suppress("UNCHECKED_CAST")
      return when (bundle.containsKey(key)) {
        true -> bundle.get(key) as T
        false -> initialValue
      }
    }

    override fun setValue(thisRef: IcePick, property: KProperty<*>, value: T) {
      val key = "icepick_${property.name}"
      val bundle = thisRef.bundle

      when (value) {
        is Boolean -> bundle.putBoolean(key, value)
        is String -> bundle.putString(key, value)
        else -> throw UnsupportedOperationException()
      }
    }
  }
}

class IcePickDelegate() : IcePick {
  override val bundle: Bundle = Bundle()
}