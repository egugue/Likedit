package com.htoyama.likit.data.common.net

import com.google.gson.GsonBuilder
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.internal.TwitterApi
import com.twitter.sdk.android.core.internal.network.OkHttpClientHelper
import com.twitter.sdk.android.core.models.BindingValues
import com.twitter.sdk.android.core.models.BindingValuesAdapter
import com.twitter.sdk.android.core.models.SafeListAdapter
import com.twitter.sdk.android.core.models.SafeMapAdapter
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by toyamaosamuyu on 2016/06/15.
 */
@Module class NetModule {

  @Provides @Singleton fun twitterCore() =
      TwitterCore.getInstance()!!

  @Provides fun okHttpClient(core: TwitterCore): OkHttpClient {
    val builder = OkHttpClientHelper.getOkHttpClientBuilder(
        core.sessionManager.activeSession,
        core.authConfig,
        core.sslSocketFactory)

    EnvDependedDelegate.onBuildOkHttpBulilder(builder)

    return builder.build()
  }

  @Provides @Singleton @Named("twitter")
  fun retrofit(okHttpClient: OkHttpClient): Retrofit {

    val gson = GsonBuilder()
        .registerTypeAdapterFactory(SafeListAdapter())
        .registerTypeAdapterFactory(SafeMapAdapter())
        .registerTypeAdapter(BindingValues::class.java, BindingValuesAdapter())
        .create()

    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(TwitterApi().baseHostUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
  }

  @Provides fun favoriteService(@Named("twitter") retrofit: Retrofit) =
      retrofit.create(FavoriteService::class.java)!!

}
