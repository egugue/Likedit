package com.htoyama.likit.common

import java.util.NoSuchElementException

sealed class Optional<out T> {
  abstract fun get(): T
  abstract fun isEmpty(): Boolean
  fun isDefined(): Boolean = !isEmpty()
}

object None : Optional<Nothing>() {
  override fun get(): Nothing = throw NoSuchElementException()
  override fun isEmpty(): Boolean = true
}

class Some<out T>(val t: T) : Optional<T>() {
  override fun get(): T = t
  override fun isEmpty(): Boolean = false
}