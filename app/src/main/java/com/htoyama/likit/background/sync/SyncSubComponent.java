package com.htoyama.likit.background.sync;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by htoyama on 2017/04/23.
 */
@Subcomponent
public interface SyncSubComponent extends AndroidInjector<TweetSyncService> {
  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<TweetSyncService> {
  }
}
