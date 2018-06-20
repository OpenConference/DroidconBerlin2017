package de.droidcon.berlin2018.ui.sessiondetails

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2018.analytics.Analytics
import de.droidcon.berlin2018.schedule.repository.SessionsRepository
import de.droidcon.berlin2018.ui.lce.LceViewState
import de.droidcon.berlin2018.ui.lce.lceObservable
import de.droidcon.berlin2018.ui.sessiondetails.SessionDetailsView.SessionState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SessionDetailsPresenter(
    private val sessionRepository: SessionsRepository,
    private val analytics: Analytics
) : MviBasePresenter<SessionDetailsView, LceViewState<SessionState>>() {

  private lateinit var addRemovFavoriteDisposable : Disposable

  override fun bindIntents() {
    //
    // No error handling nor snackbar info (added or removed to schedule). YOLO.
    //

    addRemovFavoriteDisposable = intent(SessionDetailsView::clickOnFabIntent)
        .doOnNext { Timber.d("Add Session to favorites Intent. Session = ${it.id()}") }
        .doOnNext {
          if (it.favorite()) analytics.trackSessionRemovedFromFavorite(
              it.id()) else analytics.trackSessionMarkedAsFavorite(it.id())
        }
        .switchMap {
          // So ugly, but don't know how to do it better
          Observable.fromCallable {
            if (it.favorite())
              sessionRepository.removeSessionFromSchedule(it).blockingAwait()
            else
              sessionRepository.addSessionToSchedule(it).blockingAwait()
            Unit
          }.switchMap { Observable.never<Unit>() }
        }
        .subscribe()

    val sessionData = intent(SessionDetailsView::loadIntent)
        .doOnNext { Timber.d("load Session intent. Session Id = $it") }
        .doOnNext { analytics.trackLoadSessionDetails(it) }
        .switchMap {
          // Will automatically update if Session is "favorite" / "not favorite anymore"
          lceObservable(sessionRepository.getSession(it)
              .map { SessionState(false, it) }
              .scan { old, new ->
                SessionState(
                    favoriteChanged = old.session.favorite() != new.session.favorite(),
                    session = new.session
                )
              })
        }


    subscribeViewState(sessionData, SessionDetailsView::render)
  }

  override fun unbindIntents() {
    super.unbindIntents()
    addRemovFavoriteDisposable.dispose()
  }
}
