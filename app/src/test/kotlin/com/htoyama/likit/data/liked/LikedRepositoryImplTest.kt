package com.htoyama.likit.data.liked

import android.util.LongSparseArray
import com.htoyama.likit.TweetBuilder
import com.htoyama.likit.data.liked.tweet.LikedTweetDao
import com.htoyama.likit.domain.liked.LikedBuilder
import com.htoyama.likit.domain.liked.LikedTweet
import com.htoyama.likit.domain.tag.Tag
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*
import org.mockito.Mockito.`when` as When
import rx.Observable
import com.google.common.truth.Truth.assertThat
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import rx.observers.TestSubscriber

@RunWith(RobolectricGradleTestRunner::class)
class LikedRepositoryImplTest {
  @Mock lateinit private var dao: LikedTweetDao
  @Mock lateinit private var gateway: LikedRealmGateway
  @Mock lateinit private var factory: LikedFactory
  lateinit private var impl: LikedRepositoryImpl

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
    impl = LikedRepositoryImpl(dao, gateway, factory)
  }

  @Test fun find() {
    val tweetList = Arrays.asList(
        TweetBuilder().setId(1).build())
    val tweetListObs = Observable.just(tweetList)
    When(dao.getTweetList(1, 1))
        .thenReturn(tweetListObs)

    val tagTable = LongSparseArray<List<Tag>>(1)
    tagTable.append(1, ArrayList<Tag>())
    When(gateway.getBy(tweetList))
        .thenReturn(tagTable)

    val expected = Arrays.asList(LikedBuilder().build())
    When(factory.createFrom(tagTable, tweetList))
        .thenReturn(expected)

    val actual = impl.find(1, 1)

    val sub = TestSubscriber<List<LikedTweet>>();
    actual.subscribe(sub);
    sub.awaitTerminalEvent();
    sub.assertNoErrors();
    sub.assertCompleted();
    val events = sub.onNextEvents
    assertThat(events).hasSize(1)
    assertThat(events[0]).isEqualTo(expected)
  }

}