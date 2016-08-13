package com.htoyama.likit.ui.home

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.bindView
import com.htoyama.likit.App

import com.htoyama.likit.R
import com.htoyama.likit.common.extensions.toast
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.ui.home.tag.TagCreateDialogFragment
import javax.inject.Inject

class HomeActivity : AppCompatActivity(),
    TagCreateDialogFragment.OnClickListener, HomePresenter.View {

  val component: HomeComponent by lazy {
    DaggerHomeComponent.builder()
        .appComponent(App.component(this))
        .build()
  }

  private val fab: FloatingActionButton by bindView(R.id.fab)
  private val viewPager: ViewPager by bindView(R.id.home_pager)
  private val tabLayout: TabLayout by bindView(R.id.home_tablayout)
  private val adapter: HomePagerAdapter = HomePagerAdapter(supportFragmentManager)
  @Inject lateinit var presenter: HomePresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    val toolbar = findViewById(R.id.toolbar) as Toolbar?
    setSupportActionBar(toolbar)

    component.inject(this)

    presenter.view = this

    /*
    fab.setOnClickListener {
      TagCreateDialogFragment.show(this)
    }
    */

    viewPager.adapter = adapter
    viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    viewPager.addOnPageChangeListener(FabSettingManageListener(fab, viewPager))
  }

  override fun onTagCreateButtonClick() {
    presenter.registerNewTag("foo")
    /*
    startActivity(TagTweetSelectActivity
        .createIntent(this, Tag(1, "foo", Date())))
        */
  }

  override fun showProgress() {
    toast("showProgress")
  }

  override fun hideProgress() {
    toast("hideProgress")
  }

  override fun goToTagTweetSelectScreen(tag: Tag) {
    toast("goToTagTweetSelectionScreen")
  }

}