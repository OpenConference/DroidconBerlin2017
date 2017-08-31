package de.droidcon.berlin2017.ui.myschedule

import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.MviController
import de.droidcon.berlin2017.ui.applicationComponent
import de.droidcon.berlin2017.ui.sessions.SessionsView

/**
 *
 *
 * @author Hannes Dorfmann
 */
class MyScheduleController : MviController<SessionsView, MySchedulePresenter>() {

  override val layoutRes: Int = R.layout.controller_myschedule

  override fun createPresenter(): MySchedulePresenter = MySchedulePresenter(applicationComponent().sessionsInteractor())
}