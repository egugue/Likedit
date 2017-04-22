package com.htoyama.likit;

import android.app.Service;

import com.htoyama.likit.background.sync.SyncSubComponent;
import com.htoyama.likit.background.sync.TweetSyncService;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.ServiceKey;
import dagger.multibindings.IntoMap;

/**
 * Created by htoyama on 2017/04/23.
 */

@Module
public abstract class BuildersModule {

  @Binds
  @IntoMap
  @ServiceKey(TweetSyncService.class)
  abstract AndroidInjector.Factory<? extends Service>
  bindTweetSyncServiceInjectorFactory(SyncSubComponent.Builder b);
}
