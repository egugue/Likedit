package com.egugue.licol.common.extensions

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

/**
 * Extensions for classes related to RxJava
 */

fun <T> Observable<T>.subscribeOnIo(): Observable<T> = subscribeOn(Schedulers.io())
fun <T> Observable<T>.observeOnMain(): Observable<T> = observeOn(AndroidSchedulers.mainThread())

fun <T> BehaviorRelay<T>.hasNotValue() = !hasValue()

/**
 * A extension to apply SAM conversion
 */
inline fun <T, U, R> Observable<T>.zipWith(other: ObservableSource<U>, crossinline zipper: (T, U) -> R) =
    zipWith(other, BiFunction<T, U, R> { t1, t2 -> zipper.invoke(t1, t2) })

/**
 * Basically, this method is the same as [Observable.onErrorReturn].
 * But this method doesn't wrap an error with [CompositeException].
 * Instead, it just throws the error.
 */
inline fun <T> Observable<T>.onErrorReturnOrJustThrow(
    crossinline valueSupplier: (Throwable) -> T
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

/**
 * A extension to apply SAM conversion
 */
fun <T, U, R> Single<T>.zipWith(other: SingleSource<U>, zipper: (T, U) -> R) =
    zipWith(other, BiFunction<T, U, R> { t1, t2 -> zipper.invoke(t1, t2) })
