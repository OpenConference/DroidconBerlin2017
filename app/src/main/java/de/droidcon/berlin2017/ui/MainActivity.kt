package de.droidcon.berlin2017.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import de.droidcon.berlin2017.R.id
import de.droidcon.berlin2017.R.layout
import de.droidcon.berlin2017.R.string
import de.droidcon.berlin2017.model.Session
import kotlinx.android.synthetic.main.activity_main.message
import kotlinx.android.synthetic.main.activity_main.navigation

class MainActivity : AppCompatActivity() {

  companion object {
    fun buildSessionDetailsIntent(context : Context, session : Session) : Intent {
      TODO("Implement")
    }
  }

  private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    when (item.itemId) {
      id.navigation_home -> {
        message.setText(string.title_home)
        return@OnNavigationItemSelectedListener true
      }
      id.navigation_dashboard -> {
        message.setText(string.title_dashboard)
        return@OnNavigationItemSelectedListener true
      }
      id.navigation_notifications -> {
        message.setText(string.title_notifications)
        return@OnNavigationItemSelectedListener true
      }
    }
    false
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)

    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
  }
}
