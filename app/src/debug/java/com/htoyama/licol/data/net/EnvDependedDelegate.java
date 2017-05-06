package com.htoyama.licol.data.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

class EnvDependedDelegate {

  static void onBuildOkHttpBulilder(OkHttpClient.Builder builder) {
    builder.addNetworkInterceptor(new StethoInterceptor());
  }
}
