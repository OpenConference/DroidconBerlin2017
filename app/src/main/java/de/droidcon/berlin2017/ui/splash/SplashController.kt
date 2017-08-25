package de.droidcon.berlin2017.ui.splash

import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.MviController
import de.droidcon.berlin2017.ui.applicationComponent

/**
 * Splash screen, only shown the first time the app starts
 *
 * @author Hannes Dorfmann
 */
class SplashController : MviController<SplashView, SplashPresenter>() {

  override val layoutRes: Int = R.layout.controller_splash

  override fun createPresenter(): SplashPresenter = SplashPresenter(
      applicationComponent().sessionRepository()) // Life is too short to write ton of Subcomponentes / Android injectors

}