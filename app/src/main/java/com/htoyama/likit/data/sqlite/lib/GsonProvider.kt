package com.htoyama.likit.data.sqlite.lib

import com.google.gson.GsonBuilder

object GsonProvider {
  @JvmField val gson = GsonBuilder().create()
}