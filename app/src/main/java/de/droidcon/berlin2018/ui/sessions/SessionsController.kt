package de.droidcon.berlin2018.ui.sessions

import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.ui.MviController
import de.droidcon.berlin2018.ui.applicationComponent

/**
 * Displays a list of all sessions
 *
 * @author Hannes Dorfmann
 */
class SessionsController : MviController<SessionsView, SessionsPresenter>() {

  override val layoutRes: Int = R.layout.controller_sessions

  override fun createPresenter(): SessionsPresenter = SessionsPresenter(
      applicationComponent().sessionsInteractor())
}
