package de.droidcon.berlin2017.ui.sessions


import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2017.interactor.SessionsInteractor
import de.droidcon.berlin2017.ui.lce.LceViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Presenter for [SessionsView]
 *
 * @author Hannes Dorfmann
 */
class SessionsPresenter(
    private val sessionsInteractor: SessionsInteractor
) : MviBasePresenter<SessionsView, LceViewState<Sessions>>() {

  override fun bindIntents() {
    Timber.d("Bind intents")
    val scrolledToNowIntent = intent(SessionsView::scrolledToNowIntent)
        .doOnNext { Timber.d("Scrolled to now intent: $it") }
        .distinctUntilChanged()

    val data = intent(SessionsView::loadDataIntent)
        .doOnNext { Timber.d("Load data intent") }
        .switchMap { sessionsInteractor.allSessions(scrolledToNowIntent).subscribeOn(Schedulers.io()) }
        .observeOn(AndroidSchedulers.mainThread())

    subscribeViewState(data, SessionsView::render)
  }
}