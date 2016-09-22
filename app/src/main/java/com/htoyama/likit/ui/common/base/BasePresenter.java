package com.htoyama.likit.ui.common.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Base class for all Presenter.
 *
 * a presenter inherited from it may not be {@link CompositeSubscription#clear()}
 * and release {@link VIEW}.
 * Those processes will perform when {@link #unsubscribe()} invoked.
 */
public abstract class BasePresenter<VIEW> implements Subscription, Disposable {
  @Deprecated protected final CompositeSubscription subs = new CompositeSubscription();
  protected final CompositeDisposable disposables = new CompositeDisposable();
  @Nullable protected VIEW view;

  /**
   * Register view.
   */
  public void setView(@NonNull VIEW view) {
    this.view = view;
  }

  @Deprecated
  @Override public boolean isUnsubscribed() {
    return subs.isUnsubscribed();
  }

  @Deprecated
  @Override public void unsubscribe() {
    this.view = null;
    subs.clear();
  }

  @Override public void dispose() {
    disposables.dispose();
  }

  @Override public boolean isDisposed() {
    return disposables.isDisposed();
  }
}
