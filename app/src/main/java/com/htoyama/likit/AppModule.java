package com.htoyama.likit;

import android.app.Application;
import android.content.Context;

import com.htoyama.likit.background.sync.SyncSubComponent;
import com.htoyama.likit.data.DataModule;
import com.htoyama.likit.data.sqlite.lib.SqliteOpenHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    includes = {DataModule.class},
    subcomponents = {SyncSubComponent.class})
public class AppModule {

  public AppModule() {
  }

  @Provides Application app(Application app) {
    return app;
  }
}