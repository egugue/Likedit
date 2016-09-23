package com.htoyama.likit.data.liked

import com.htoyama.likit.TweetBuilder
import com.htoyama.likit.data.liked.tweet.LikedTweetDao
import com.htoyama.likit.domain.liked.LikedBuilder
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*
import org.mockito.Mockito.`when` as When
import com.google.common.truth.Truth.assertThat
import io.reactivex.Single

class LikedRepositoryImplTest {
  @Mock lateinit private var dao: LikedTweetDao
  @Mock lateinit private var gateway: LikedRealmGateway
  lateinit private var impl: LikedRepositoryImpl

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
    impl = LikedRepositoryImpl(dao, gateway)
  }

  @Test fun find() {
    val tweetList = Arrays.asList(
        TweetBuilder().setId(1).build())
    val tweetListObs = Single.just(tweetList)
    When(dao.getTweetList(1, 1))
        .thenReturn(tweetListObs)

    val expected = Arrays.asList(LikedBuilder().build())
    When(gateway.getBy(tweetList))
        .thenReturn(expected)

    val test = impl.find(1, 1).test()
    test.awaitTerminalEvent()

    test.assertComplete()
    val events = test.values()
    assertThat(events).hasSize(1)
    assertThat(events[0]).isEqualTo(expected)
  }

}