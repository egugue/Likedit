package com.htoyama.licol.ui.home

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import butterknife.bindView
import com.htoyama.licol.App

import com.htoyama.licol.R
import com.htoyama.licol.ui.search.SearchActivity

class HomeActivity : AppCompatActivity() {

  val component: HomeComponent by lazy {
    DaggerHomeComponent.builder()
        .appComponent(App.component(this))
        .build()
  }

  private val fab: FloatingActionButton by bindView(R.id.fab)
  private val viewPager: ViewPager by bindView(R.id.home_pager)
  private val tabLayout: TabLayout by bindView(R.id.home_tablayout)
  private val adapter: HomePagerAdapter = HomePagerAdapter(supportFragmentManager)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    val toolbar = findViewById(R.id.toolbar) as Toolbar?
    setSupportActionBar(toolbar)

    viewPager.adapter = adapter
    viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    viewPager.addOnPageChangeListener(FabSettingManageListener(fab, viewPager))
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