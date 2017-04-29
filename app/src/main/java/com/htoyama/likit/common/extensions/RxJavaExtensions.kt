package com.htoyama.likit.common.extensions

import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Extensions for classes related to RxJava
 */

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
