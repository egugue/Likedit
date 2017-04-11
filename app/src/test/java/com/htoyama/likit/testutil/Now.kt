package com.htoyama.likit.testutil

/**
 * An annotation which defines the current timemills which is returned by [CurrentTime.get]
 */
@Retention
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Now(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
    val nanoOfSecond: Int = 0
)
