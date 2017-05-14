package com.egugue.licol.common.extensions

import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.*
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ThrowableExtensionsKtTest {

  @Test fun isTwitterRateLimitException() {
    val rateLimit = HttpException(Response.error<Any>(429, mock()))
    assertTrue(rateLimit.isTwitterRateLimitException())

    val aException = Exception()
    assertFalse(aException.isTwitterRateLimitException())
  }

}