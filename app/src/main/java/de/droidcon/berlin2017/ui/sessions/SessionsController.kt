package de.droidcon.berlin2017.ui.sessions

import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.MviController
import de.droidcon.berlin2017.ui.applicationComponent

/**
 * Displays a list of all sessions
 *
 * @author Hannes Dorfmann
 */
class SessionsController : MviController<SessionsView, SessionsPresenter>() {

  override val layoutRes: Int = R.layout.controller_lce

  override fun createPresenter(): SessionsPresenter = SessionsPresenter(
      applicationComponent().sessionsInteractor())
}