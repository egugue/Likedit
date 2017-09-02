package com.egugue.licol.ui.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.Menu
import android.view.MenuItem
import com.egugue.licol.App
import com.egugue.licol.R
import com.egugue.licol.common.analytics.Analytics
import com.egugue.licol.common.analytics.ViewEvent
import com.egugue.licol.ui.common.base.BaseActivity
import com.egugue.licol.ui.common.customtabs.CustomTabActivityHelper
import com.egugue.licol.ui.search.SearchActivity
import kotlinx.android.synthetic.main.home_activity.home_tablayout
import kotlinx.android.synthetic.main.home_activity.toolbar
import kotlinx.android.synthetic.main.home_activity.view_pager
import javax.inject.Inject

class HomeActivity : BaseActivity() {

  val component: HomeComponent by lazy {
    DaggerHomeComponent.builder()
        .appComponent(App.component(this))
        .build()
  }

  @Inject lateinit var analytics: Analytics
  @Inject lateinit var customTabActivityHelper: CustomTabActivityHelper

  internal val adapter: HomePagerAdapter = HomePagerAdapter(supportFragmentManager)

  override fun onCreate(savedInstanceState: Bundle?) {
    component.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.home_activity)

    setSupportActionBar(toolbar)
    view_pager.adapter = adapter
    view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(home_tablayout))

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