package com.htoyama.likit.common.extensions

import retrofit2.HttpException

/**
 * Extensions for exceptions such as [Exception] or [Throwable].
 */

/**
 * @see [API Rate Limits](https://dev.twitter.com/rest/public/rate-limiting)
 */
fun Throwable.isTwitterRateLimitException(): Boolean
    = this is HttpException && this.code() == 429
