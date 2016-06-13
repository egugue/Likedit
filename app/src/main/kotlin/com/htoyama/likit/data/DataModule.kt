package com.htoyama.likit.data

import com.htoyama.likit.data.pref.PrefModule
import dagger.Module

@Module(includes = arrayOf(PrefModule::class))
class DataModule
