package com.htoyama.licol.common.extensions

import io.reactivex.Observable
import org.junit.Test

class RxJavaExtensionsKtTest {

  @Test fun onErrorReturnOrJustThrow_conditionMatches() {
    Observable.error<String>(IllegalArgumentException())
        .onErrorReturnOrJustThrow {
          if (it is IllegalArgumentException) "it is expected" else throw it
        }
        .test()
        .assertValue("it is expected")
        .assertComplete()
        .assertNoErrors()
  }

  @Test fun onErrorReturnOrJustThrow_conditionDoesNotMatches() {
    val expected = Exception()
    Observable.error<String>(expected)
        .onErrorReturnOrJustThrow {
          if (it is IllegalArgumentException) "it's not expected" else throw it
        }
        .test()
        .assertNoValues()
        .assertNotComplete()
        .assertError(expected)
  }
}