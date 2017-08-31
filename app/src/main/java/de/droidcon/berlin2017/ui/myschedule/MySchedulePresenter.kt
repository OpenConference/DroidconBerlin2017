package de.droidcon.berlin2017.ui.myschedule

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2017.interactor.SessionsInteractor
import de.droidcon.berlin2017.ui.lce.LceViewState
import de.droidcon.berlin2017.ui.sessions.Sessions
import de.droidcon.berlin2017.ui.sessions.SessionsView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 *
 *
 * @author Hannes Dorfmann
 */
class MySchedulePresenter(
    private val sessionsInteractor: SessionsInteractor
) : MviBasePresenter<SessionsView, LceViewState<Sessions>>() {

  override fun bindIntents() {
    val scrolledToNowIntent = intent(SessionsView::scrolledToNowIntent)
        .doOnNext { Timber.d("Scrolled to now intent: $it") }

    val data = intent(SessionsView::loadDataIntent)
        .doOnNext { Timber.d("Load data intent") }
        .switchMap { sessionsInteractor.favoriteSessions(scrolledToNowIntent).subscribeOn(Schedulers.io()) }
        .observeOn(AndroidSchedulers.mainThread())

    subscribeViewState(data, SessionsView::render)
  }
}