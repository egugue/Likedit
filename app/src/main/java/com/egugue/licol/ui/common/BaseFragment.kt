package com.egugue.licol.ui.common

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.support.v4.app.Fragment
import com.trello.rxlifecycle2.components.support.RxFragment

/**
 * Created by htoyama on 2017/05/18.
 */
open class BaseFragment : RxFragment(), LifecycleRegistryOwner {
  private val registry = LifecycleRegistry(this)

  override fun getLifecycle(): LifecycleRegistry = registry
}