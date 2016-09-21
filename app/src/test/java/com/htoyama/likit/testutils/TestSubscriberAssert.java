package com.htoyama.likit.testutils;

import com.google.common.truth.FailureStrategy;
import com.google.common.truth.Subject;
import com.google.common.truth.SubjectFactory;

import javax.annotation.Nullable;

import rx.Observable;
import rx.observers.TestSubscriber;

import static com.google.common.truth.Truth.assertAbout;

public final class TestSubscriberAssert<T>
    extends Subject<TestSubscriberAssert<T>, TestSubscriber<T>> {

  public TestSubscriberAssert(FailureStrategy failureStrategy, @Nullable TestSubscriber<T> subject) {
    super(failureStrategy, subject);
  }

  public static <T> TestSubscriberAssert<T> assertThat(TestSubscriber<T> subscriber) {
    return assertAbout(new SubjectFactory<TestSubscriberAssert<T>, TestSubscriber<T>>() {
      @Override
      public TestSubscriberAssert<T> getSubject(FailureStrategy fs, TestSubscriber<T> that) {
        return new TestSubscriberAssert<>(fs, that);
      }
    }).that(subscriber);
  }

  public static <T> TestSubscriberAssert<T> assertThatAsSubscriberTo(Observable<T> observable) {
    TestSubscriber<T> sub = new TestSubscriber<>();
    observable.subscribe(sub);
    return assertThat(sub);
  }

  public TestSubscriberAssert reviceved(T... values) {
    TestSubscriber<T> subject = getSubject();

    subject.assertValues(values);
    subject.assertCompleted();
    subject.assertNoErrors();
    return this;
  }

}
