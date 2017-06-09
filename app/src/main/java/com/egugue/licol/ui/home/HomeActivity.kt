package com.egugue.licol.ui.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import butterknife.bindView
import com.egugue.licol.App

import com.egugue.licol.R
import com.egugue.licol.common.analytics.Analytics
import com.egugue.licol.common.analytics.ViewEvent
import com.egugue.licol.ui.common.activity.BaseActivity
import com.egugue.licol.ui.common.customtabs.CustomTabActivityHelper
import com.egugue.licol.ui.search.SearchActivity
import javax.inject.Inject

class HomeActivity : BaseActivity() {

  val component: HomeComponent by lazy {
    DaggerHomeComponent.builder()
        .appComponent(App.component(this))
        .build()
  }

  @Inject lateinit var analytics: Analytics
  @Inject lateinit var customTabActivityHelper: CustomTabActivityHelper

  private val toolbar: Toolbar by bindView(R.id.toolbar)
  private val viewPager: ViewPager by bindView(R.id.home_pager)
  private val tabLayout: TabLayout by bindView(R.id.home_tablayout)
  private val adapter: HomePagerAdapter = HomePagerAdapter(supportFragmentManager)

  override fun onCreate(savedInstanceState: Bundle?) {
    component.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    setSupportActionBar(toolbar)
    viewPager.adapter = adapter
    viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

    analytics.viewEvent(ViewEvent.HOME)
    customTabActivityHelper.setUpCustomTabService(this)
  }

  override fun onResume() {
    super.onResume()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_home, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.app_bar_search ->
        startActivity(SearchActivity.createIntent(this))
    }

    return super.onOptionsItemSelected(item)
  }
}