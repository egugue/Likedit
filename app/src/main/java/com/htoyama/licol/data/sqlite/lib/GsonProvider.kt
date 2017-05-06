package com.htoyama.licol.data.sqlite.lib

import com.google.gson.GsonBuilder

object GsonProvider {
  @JvmField val gson = GsonBuilder().create()
}