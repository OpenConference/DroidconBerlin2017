package de.droidcon.berlin2017.ui.sessions

import com.hannesdorfmann.mosby3.mvp.MvpView
import de.droidcon.berlin2017.ui.lce.LceViewState
import io.reactivex.Observable

/**
 * Displays a list of all sessions
 *
 * @author Hannes Dorfmann
 */
interface SessionsView : MvpView {

  /**
   * Intent to load the data
   */
  fun loadDataIntent(): Observable<Unit>

  /**
   * Indicates that the UI has automatically scrolled to now
   */
  fun scrolledToNowIntent(): Observable<Boolean>

  /**
   * Render the sessions on screen
   */
  fun render(state: LceViewState<Sessions>)
}