package com.htoyama.likit.data

import com.htoyama.likit.data.common.net.NetModule
import com.htoyama.likit.data.common.pref.PrefModule
import dagger.Module

@Module(includes = arrayOf(
    PrefModule::class,
    NetModule::class
))
class DataModule
