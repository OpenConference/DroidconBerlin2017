package de.droidcon.berlin2017.ui.myschedule

import de.droidcon.berlin2017.ui.sessions.SessionsViewBinding

/**
 * ViewBinding for MySchedule view
 *
 * @author Hannes Dorfmann
 */
class MyScheduleViewBinding : SessionsViewBinding() {

  override fun onEmptyViewClicked() {
    navigator.showSessions()
  }
}