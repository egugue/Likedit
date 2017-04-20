package com.htoyama.likit.common.extensions

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Extensions for classes related to RxJava
 */
fun <T> Observable<T>.onErrorReturnOrJustThrow(
    predicate: (Throwable) -> Boolean,
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
      if (predicate.invoke(e)) {
        it.onNext(valueSupplier.invoke(e))
        it.onComplete()
      } else {
        it.onError(e)
      }
    }
  }
})
