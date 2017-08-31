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
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import de.droidcon.berlin2017.DroidconApplication
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.schedule.backend.ScheduleDataStateDeterminer.ScheduleDataState.NO_DATA
import de.droidcon.berlin2017.ui.goodbye.GoodByeController
import de.droidcon.berlin2017.ui.home.HomeController
import de.droidcon.berlin2017.ui.sessiondetails.SessionDetailsController
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
      else {
        router.setRoot(RouterTransaction.with(HomeController()))

        if (intent != null) {
          val sessionId = intent.getStringExtra(KEY_SESSION_ID)
          if (sessionId != null) {
            // Activity has been started from a notification
            Timber.d("Application started from a Notification. Showing session id = $sessionId")
            router.pushController(RouterTransaction.with(SessionDetailsController(sessionId))
                .popChangeHandler(HorizontalChangeHandler()))
          }
        }
      }
    }

    updateCheckerDisposable = applicationComponent.appUpdaterChecker()
        .newAppVersionAvailable()
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe({
          if (!it.appPublished) {
            router.setRoot(RouterTransaction.with(GoodByeController()))
          } else if (it.newerAppVersionAvailable) {
            if (router.getControllerWithTag("UpdateDialog") == null) {
              router.pushController(RouterTransaction.with(UpdateController())
                  .pushChangeHandler(FadeChangeHandler())
                  .popChangeHandler(FadeChangeHandler())
                  .tag("UpdateDialog")
              )
            }
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
    Timber.d("Received new intent")
    if (intent != null) {
      val sessionId = intent.getStringExtra(KEY_SESSION_ID)
      if (sessionId == null) {
        Timber.e(IllegalStateException("Got new intent without a session id"))
      } else {
        Timber.d("Gonna show session with id = $sessionId")
        val applicationComponent = DroidconApplication.getApplicationComponent(this)
        applicationComponent.analytics().trackSessionNotificationOpened(sessionId)
        router.pushController(
            RouterTransaction.with(SessionDetailsController(sessionId))
                .pushChangeHandler(HorizontalChangeHandler())
                .popChangeHandler(HorizontalChangeHandler())
        )
      }
    }
  }
}
