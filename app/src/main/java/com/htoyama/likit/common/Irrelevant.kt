package com.htoyama.likit.common

/**
 * The class repesenting irrevalent object, which be useful in rxjava.
 */
class Irrelevant private constructor() {

  companion object {
    private val INSTANCE = Irrelevant()

    /** Retrieve the instance. */
    @JvmStatic fun get(): Any {
      return INSTANCE
    }
  }
}
