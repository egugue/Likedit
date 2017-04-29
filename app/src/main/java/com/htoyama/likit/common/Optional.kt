package com.htoyama.likit.common

import java.util.NoSuchElementException

sealed class Optional<out T> {
  abstract fun get(): T
  abstract fun isEmpty(): Boolean
  fun isDefined(): Boolean = !isEmpty()
}

object None : Optional<Nothing>() {
  override fun get(): Nothing = throw NoSuchElementException("None.get invoked.")
  override fun isEmpty(): Boolean = true
}

class Some<out T>(val t: T) : Optional<T>() {
  override fun get(): T = t
  override fun isEmpty(): Boolean = false

  override fun equals(other: Any?): Boolean = when (other) {
    is Some<*> -> t == other.get()
    else -> false
  }

  override fun hashCode(): Int {
    return t?.hashCode() ?: 0
  }
}

fun <T> T?.toOptional(): Optional<T> = if (this == null) None else Some(this)