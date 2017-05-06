package com.htoyama.licol.ui.common.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Base class for all Presenter.
 *
 * a presenter inherited from it may not be {@link CompositeDisposable#clear()}
 * and release {@link VIEW}.
 * Those processes will perform when {@link #dispose()} invoked.
 */
public abstract class BasePresenter<VIEW> implements Disposable {
  protected final CompositeDisposable disposables = new CompositeDisposable();
  @Nullable protected VIEW view;

  /**
   * Register view.
   */
  public void setView(@NonNull VIEW view) {
    this.view = view;
  }

  @Override public void dispose() {
    this.view = null;
    disposables.clear();
  }

  @Override public boolean isDisposed() {
    return disposables.isDisposed();
  }
}
