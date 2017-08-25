package de.droidcon.berlin2017.ui.home

import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.MviController

/**
 * This is just the container for bottom navigation etc.
 *
 * @author Hannes Dorfmann
 */
class HomeController : MviController<HomeView, HomePresenter>() {

  // No tracking --> each controller in BottomNavigation will be tracked individually once shown
  override val trackingWithAnalytics = false

  override val layoutRes: Int = R.layout.controller_home

  override fun createPresenter(): HomePresenter =
      HomePresenter()
}