package de.droidcon.berlin2018.ui.barcamp

import android.support.v4.widget.SwipeRefreshLayout
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.ui.gone
import de.droidcon.berlin2018.ui.lce.LceViewState
import de.droidcon.berlin2018.ui.lce.LceViewState.Content
import de.droidcon.berlin2018.ui.sessions.Sessions
import de.droidcon.berlin2018.ui.sessions.SessionsViewBinding
import de.droidcon.berlin2018.ui.viewbinding.LifecycleAwareRef
import de.droidcon.berlin2018.ui.visible
import io.reactivex.Observable

/**
 *
 *
 * @author Hannes Dorfmann
 */
class BarCampViewBinding : SessionsViewBinding(), BarCampView {


  override val loggingTag: String = BarCampViewBinding::class.java.simpleName

  private var swipeRefreshLayout by LifecycleAwareRef<SwipeRefreshLayout>(this)

  override fun bindView(rootView: ViewGroup) {
    super.bindView(rootView)
    swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh)
    swipeRefreshLayout.setProgressViewOffset(false, 0,
        rootView.resources!!.getDimensionPixelOffset(R.dimen.subheader_height_plus_searchbox))
  }

  override fun swipeRefreshIntent(): Observable<Unit> = swipeRefreshLayout.refreshes()

  override fun onSessionClicked(session: Session) {

  }


  override fun render(state: LceViewState<Sessions>) {
    super.render(state)

    swipeRefreshLayout.isRefreshing = false
    when (state) {
      is Content -> {
        swipeRefreshLayout.visible()
      }
      else -> swipeRefreshLayout.gone()
    }
  }

}
