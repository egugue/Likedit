package com.htoyama.likit.ui.common.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * A {@link BaseActivity} which is useful when using Reactive Extensions.
 */
public abstract class BaseRxActivity extends BaseActivity implements LifecycleProvider<ActivityEvent> {

  private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

  @Nonnull @Override public final Observable<ActivityEvent> lifecycle() {
    return lifecycleSubject.asObservable();
  }

  @Nonnull @Override
  public final <T> LifecycleTransformer<T> bindUntilEvent(@Nonnull ActivityEvent event) {
    return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
  }

  @Nonnull @Override public final <T> LifecycleTransformer<T> bindToLifecycle() {
    return RxLifecycleAndroid.bindActivity(lifecycleSubject);
  }

  @Override
  @CallSuper
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    lifecycleSubject.onNext(ActivityEvent.CREATE);
  }

  @Override
  @CallSuper
  protected void onStart() {
    super.onStart();
    lifecycleSubject.onNext(ActivityEvent.START);
  }

  @Override
  @CallSuper
  protected void onResume() {
    super.onResume();
    lifecycleSubject.onNext(ActivityEvent.RESUME);
  }

  @Override
  @CallSuper
  protected void onPause() {
    lifecycleSubject.onNext(ActivityEvent.PAUSE);
    super.onPause();
  }

  @Override
  @CallSuper
  protected void onStop() {
    lifecycleSubject.onNext(ActivityEvent.STOP);
    super.onStop();
  }

  @Override
  @CallSuper
  protected void onDestroy() {
    lifecycleSubject.onNext(ActivityEvent.DESTROY);
    super.onDestroy();
  }
}
