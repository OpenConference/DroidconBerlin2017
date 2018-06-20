package de.droidcon.berlin2018.ui.sessions


import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2018.interactor.SessionsInteractor
import de.droidcon.berlin2018.ui.lce.LceViewState
import io.reactivex.Observable
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
    val scrolledToNowIntent = intent(SessionsView::scrolledToNowIntent)
        .doOnNext { Timber.d("Scrolled to now intent: $it") }
        .distinctUntilChanged()

    val loadDataIntent = intent(SessionsView::loadDataIntent)
        .doOnNext { Timber.d("Load data intent") }

    val retryLoadDataIntent = intent(SessionsView::retryLoadDataIntent)
        .doOnNext { Timber.d("Retry load data intent") }

    val data = Observable.merge(loadDataIntent, retryLoadDataIntent)
        .switchMap {
          sessionsInteractor.allSessions(scrolledToNowIntent).subscribeOn(Schedulers.io())
        }
        .observeOn(AndroidSchedulers.mainThread())

    subscribeViewState(data, SessionsView::render)
  }
}
