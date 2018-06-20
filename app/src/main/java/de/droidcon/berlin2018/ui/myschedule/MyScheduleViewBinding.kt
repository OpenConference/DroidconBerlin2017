package de.droidcon.berlin2018.ui.myschedule

import de.droidcon.berlin2018.ui.sessions.SessionsViewBinding

/**
 * ViewBinding for MySchedule view
 *
 * @author Hannes Dorfmann
 */
class MyScheduleViewBinding : SessionsViewBinding() {

  override val loggingTag: String = MyScheduleViewBinding::class.java.simpleName

  override fun onEmptyViewClicked() {
    navigator.showSessions()
  }
}
