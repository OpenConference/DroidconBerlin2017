package de.droidcon.berlin2018.ui.sessiondetails

import com.hannesdorfmann.mosby3.mvp.MvpView
import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.ui.lce.LceViewState
import io.reactivex.Observable

/**
 *
 *
 * @author Hannes Dorfmann
 */
interface SessionDetailsView : MvpView {

  /**
   * The session id is emitted
   */
  fun loadIntent(): Observable<String>

  /**
   * Emits the session id to put the session into the users schedule
   */
  fun clickOnFabIntent(): Observable<Session>


  /**
   * Renders the session on screen
   */
  fun render(state: LceViewState<SessionState>)


  /**
   * View state for a session
   */
  data class SessionState (
      val favoriteChanged : Boolean,
      val session : Session
  )
}
