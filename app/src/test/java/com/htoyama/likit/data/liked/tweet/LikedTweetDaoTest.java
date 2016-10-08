package com.htoyama.likit.data.liked.tweet;

import com.htoyama.likit.TweetBuilder;
import com.htoyama.likit.data.common.net.FavoriteService;
import com.htoyama.likit.data.liked.LikedRealmGateway;
import com.htoyama.likit.data.liked.tweet.cache.LikedTweetCacheGateway;
import com.htoyama.likit.data.tweet.TweetMapper;
import com.htoyama.likit.domain.tweet.Tweet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LikedTweetDaoTest {
  @Mock FavoriteService favoriteService;
  @Mock LikedTweetCacheGateway cacheGateway;
  @Mock LikedRealmGateway likedRealmGateway;
  private LikedTweetDao dao;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    dao = new LikedTweetDao(favoriteService, cacheGateway, likedRealmGateway, new TweetMapper());
  }

  @Test public void getTweetList_whenHavingCache() {
    // given
    int page = 1;
    int count = 1;
    List<Tweet> fromCache = Arrays.asList(new TweetBuilder().build());
    List<com.twitter.sdk.android.core.models.Tweet> fromNetwork = Collections.emptyList();

    when(cacheGateway.getList(page, count))
        .thenReturn(Single.just(fromCache));
    when(favoriteService.list(null, null, count, null, null, true, page))
        .thenReturn(Single.just(fromNetwork));

    // when
    TestObserver<List<Tweet>> test = dao.getTweetList(page, count).test();
    test.awaitTerminalEvent();

    // then
    test.assertValue(fromCache);
    verify(likedRealmGateway, never())
        .insertAsContainingNoTag(any(fromCache.getClass()));
  }

  @Ignore("Creating Tweet model is hard.")
  @Test public void getTweetList_whenNoCache() {
    // given
    int page = 1;
    int count = 1;
    List<Tweet> noCache = Collections.emptyList();
    List<com.twitter.sdk.android.core.models.Tweet> fromNetwork = Arrays.asList(

    );

    when(cacheGateway.getList(page, count))
        .thenReturn(Single.just(noCache));
    when(favoriteService.list(null, null, count, null, null, true, page))
        .thenReturn(Single.just(fromNetwork));

    // when
    TestObserver<List<Tweet>> test = dao.getTweetList(page, count).test();
    test.awaitTerminalEvent();

    // then
    test.assertValueCount(1);
    verify(likedRealmGateway)
        .insertAsContainingNoTag(any(noCache.getClass()));

  }

}
