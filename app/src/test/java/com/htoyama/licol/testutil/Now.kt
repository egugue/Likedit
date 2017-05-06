package com.htoyama.licol.testutil

/**
 * An annotation which defines the current timemills which is returned by [CurrentTime.get]
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Now(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
    val nanoOfSecond: Int = 0
)
