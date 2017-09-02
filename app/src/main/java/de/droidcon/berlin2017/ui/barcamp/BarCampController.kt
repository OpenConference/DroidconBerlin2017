package de.droidcon.berlin2017.ui.barcamp

import `in`.uncod.android.bypass.Bypass
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.MviController
import de.droidcon.berlin2017.ui.applicationComponent

/**
 *
 *
 * @author Hannes Dorfmann
 */
class BarCampController : MviController<BarCampView, BarCampPresenter>() {


  override val layoutRes: Int = R.layout.controller_sessions


  override fun createPresenter(): BarCampPresenter = BarCampPresenter(
      applicationComponent().okHttp(), applicationComponent().clock(), Bypass(applicationContext!!),
      applicationComponent().sessionsInteractor())
}