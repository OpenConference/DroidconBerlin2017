package de.droidcon.berlin2017.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import de.droidcon.berlin2017.DroidconApplication
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.ui.splash.SplashController


class MainActivity : AppCompatActivity() {

  companion object {
    fun buildSessionDetailsIntent(context: Context, session: Session): Intent {
      TODO("Implement")
    }
  }
  private lateinit var router: Router

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    val container = findViewById<ViewGroup>(R.id.controller_container)

    router = Conductor.attachRouter(this, container, savedInstanceState)
    if (!router.hasRootController()) {
      val showSplash = DroidconApplication.getApplicationComponent(this).scheduleStateDeterminer()
          .getScheduleSyncDataState().blockingGet()

    //  if (showSplash == NO_DATA)
        router.setRoot(RouterTransaction.with(SplashController()))
    //  else
    //    router.setRoot(RouterTransaction.with(HomeController()))
    }
  }

  override fun onBackPressed() {
    if (!router.handleBack()) {
      super.onBackPressed()
    }
  }
}
