package com.htoyama.likit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.htoyama.likit.ui.auth.AuthActivity

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById(R.id.text_view)?.setOnClickListener {
      startActivity(AuthActivity.createIntent(this))
    }
  }

}
