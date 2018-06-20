package de.droidcon.berlin2018.ui.myschedule

import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.ui.MviController
import de.droidcon.berlin2018.ui.applicationComponent
import de.droidcon.berlin2018.ui.sessions.SessionsView

/**
 *
 *
 * @author Hannes Dorfmann
 */
class MyScheduleController : MviController<SessionsView, MySchedulePresenter>() {

  override val layoutRes: Int = R.layout.controller_myschedule

  override fun createPresenter(): MySchedulePresenter = MySchedulePresenter(applicationComponent().sessionsInteractor())
}
