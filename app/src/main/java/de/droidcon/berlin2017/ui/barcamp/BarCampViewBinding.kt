package de.droidcon.berlin2017.ui.barcamp

import de.droidcon.berlin2017.ui.sessions.SessionsViewBinding
import io.reactivex.Observable

/**
 *
 *
 * @author Hannes Dorfmann
 */
class BarCampViewBinding : SessionsViewBinding(), BarCampView {


  override val loggingTag: String = BarCampViewBinding::class.java.simpleName

  override fun swipeRefreshIntent(): Observable<Unit> {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}