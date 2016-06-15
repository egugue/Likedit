package com.htoyama.likit.data.common.net

import com.google.gson.GsonBuilder
import com.twitter.sdk.android.core.AuthenticatedClient
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.internal.TwitterApi
import com.twitter.sdk.android.core.models.SafeListAdapter
import com.twitter.sdk.android.core.models.SafeMapAdapter
import dagger.Module
import dagger.Provides
import retrofit.RestAdapter
import retrofit.converter.GsonConverter
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by toyamaosamuyu on 2016/06/15.
 */
@Module class NetModule {

  @Provides @Singleton fun twitterCore(): TwitterCore
      = TwitterCore.getInstance()

  @Provides fun authenticatedClient(core: TwitterCore): AuthenticatedClient =
      AuthenticatedClient(
          core.authConfig,
          core.sessionManager.activeSession,
          core.sslSocketFactory)

  @Provides @Singleton @Named("twitter")
  fun twitterAdapter(client: AuthenticatedClient): RestAdapter {
    val gson = GsonBuilder()
        .registerTypeAdapterFactory(SafeListAdapter())
        .registerTypeAdapterFactory(SafeMapAdapter())
        .create();

    return RestAdapter.Builder()
        .setClient(client)
        .setEndpoint(TwitterApi().baseHostUrl)
        .setConverter(GsonConverter(gson))
        .build()
  }

  @Provides fun favoriteService(@Named("twitter") adapter: RestAdapter): FavoriteService =
      adapter.create(FavoriteService::class.java)

}
