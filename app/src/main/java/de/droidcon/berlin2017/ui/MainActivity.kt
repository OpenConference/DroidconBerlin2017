package de.droidcon.berlin2017.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import de.droidcon.berlin2017.DroidconApplication
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.schedule.backend.ScheduleDataStateDeterminer.ScheduleDataState.NO_DATA
import de.droidcon.berlin2017.ui.goodbye.GoodByeController
import de.droidcon.berlin2017.ui.home.HomeController
import de.droidcon.berlin2017.ui.splash.SplashController
import de.droidcon.berlin2017.ui.update.UpdateController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber


class MainActivity : AppCompatActivity() {

  companion object {
    private val KEY_SESSION_ID = "SessionIdFromNotification"
    fun buildSessionDetailsIntent(context: Context, session: Session): Intent {
      val intent = Intent(context, MainActivity::class.java)
      intent.putExtra(KEY_SESSION_ID, session.id())
      return intent
    }
  }

  private lateinit var router: Router
  private lateinit var updateCheckerDisposable: Disposable

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    val container = findViewById<ViewGroup>(R.id.controller_container)
    val applicationComponent = DroidconApplication.getApplicationComponent(this)

    router = Conductor.attachRouter(this, container, savedInstanceState)
    if (!router.hasRootController()) {
      val showSplash = applicationComponent.scheduleStateDeterminer()
          .getScheduleSyncDataState().blockingGet()

      if (showSplash == NO_DATA)
        router.setRoot(RouterTransaction.with(SplashController()))
      else
        router.setRoot(RouterTransaction.with(HomeController()))
    }

    updateCheckerDisposable = applicationComponent.appUpdaterChecker()
        .newAppVersionAvailable()
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe({
          if (!it.appPublished) {
            router.setRoot(RouterTransaction.with(GoodByeController()))
          } else if (it.newerAppVersionAvailable) {
            router.pushController(RouterTransaction.with(UpdateController())
                .pushChangeHandler(FadeChangeHandler())
                .popChangeHandler(FadeChangeHandler())
            )
          }
        }, { Timber.e(it) })
  }

  override fun onBackPressed() {
    if (!router.handleBack()) {
      super.onBackPressed()
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    // TODO implement notification handling
  }
}
