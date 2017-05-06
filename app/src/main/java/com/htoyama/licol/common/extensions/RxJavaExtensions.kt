package com.htoyama.licol.common.extensions

import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction

/**
 * Extensions for classes related to RxJava
 */

/**
 * A extension to apply SAM conversion
 */
fun <T, U, R> Observable<T>.zipWith(other: ObservableSource<U>, zipper: (T, U) -> R) =
    zipWith(other, BiFunction<T, U, R> { t1, t2 -> zipper.invoke(t1, t2) })

/**
 * Basically, this method is the same as [Observable.onErrorReturn].
 * But this method doesn't wrap an error with [CompositeException].
 * Instead, it just throws the error.
 */
fun <T> Observable<T>.onErrorReturnOrJustThrow(
    valueSupplier: (Throwable) -> T
): Observable<T> = lift({
  object : Observer<T> {
    override fun onComplete() {
      it.onComplete()
    }

    override fun onNext(t: T) {
      it.onNext(t)
    }

    override fun onSubscribe(d: Disposable) {
      it.onSubscribe(d)
    }

    override fun onError(e: Throwable) {
      try {
          it.onNext(valueSupplier.invoke(e))
          it.onComplete()
      } catch (t: Throwable) {
        it.onError(t)
      }
    }
  }
})

fun <T> rx.Observable<T>.toV2Observable(): Observable<T> =
    RxJavaInterop.toV2Observable(this)

/**
 * A extension to apply SAM conversion
 */
fun <T, U, R> Single<T>.zipWith(other: SingleSource<U>, zipper: (T, U) -> R) =
    zipWith(other, BiFunction<T, U, R> { t1, t2 -> zipper.invoke(t1, t2) })
