package de.droidcon.berlin2017.ui.barcamp

import de.droidcon.berlin2017.ui.sessions.SessionsView
import io.reactivex.Observable

/**
 *
 *
 * @author Hannes Dorfmann
 */
interface BarCampView : SessionsView {

  fun swipeRefreshIntent() : Observable<Unit>

}