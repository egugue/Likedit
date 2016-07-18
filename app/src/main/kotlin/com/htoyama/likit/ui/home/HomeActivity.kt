package com.htoyama.likit.ui.home

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.htoyama.likit.App

import com.htoyama.likit.R

class HomeActivity : AppCompatActivity() {

  val component: HomeComponent by lazy {
    DaggerHomeComponent.builder()
        .appComponent(App.component(this))
        .build()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    val toolbar = findViewById(R.id.toolbar) as Toolbar?
    setSupportActionBar(toolbar)

    val fab = findViewById(R.id.fab) as FloatingActionButton?
    fab!!.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

    val tabLayout = findViewById(R.id.home_tablayout) as TabLayout?
    val pager = findViewById(R.id.home_pager) as ViewPager?
    val adapter = HomePagerAdapter(supportFragmentManager)
    pager!!.adapter = adapter
    pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
  }

}