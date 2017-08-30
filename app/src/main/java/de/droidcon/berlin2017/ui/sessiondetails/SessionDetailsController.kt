package de.droidcon.berlin2017.ui.sessiondetails

import android.os.Bundle
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.MviController
import de.droidcon.berlin2017.ui.applicationComponent

/**
 * Displays the details for a certain Session
 *
 * @author Hannes Dorfmann
 */
class SessionDetailsController(
    args: Bundle?) : MviController<SessionDetailsView, SessionDetailsPresenter>(args) {

  companion object {
    val KEY_SESSION_ID = "SessionId"
  }

  constructor(sessionId: String) : this(Bundle().also { it.putString(KEY_SESSION_ID, sessionId) })


  override val layoutRes: Int = R.layout.controller_session_details

  override fun createPresenter(): SessionDetailsPresenter = SessionDetailsPresenter(
      applicationComponent().sessionRepository(), applicationComponent().analytics())

}