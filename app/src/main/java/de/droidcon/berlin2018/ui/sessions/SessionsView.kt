package de.droidcon.berlin2018.ui.sessions

import com.hannesdorfmann.mosby3.mvp.MvpView
import de.droidcon.berlin2018.ui.lce.LceViewState
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
   * Retry to load data
   */
  fun retryLoadDataIntent() : Observable<Unit>

  /**
   * Indicates that the UI has automatically scrolled to now
   */
  fun scrolledToNowIntent(): Observable<Boolean>

  /**
   * Render the sessions on screen
   */
  fun render(state: LceViewState<Sessions>)
}
