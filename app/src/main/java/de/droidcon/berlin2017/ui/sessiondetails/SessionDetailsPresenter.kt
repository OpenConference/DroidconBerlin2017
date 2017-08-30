package de.droidcon.berlin2017.ui.sessiondetails

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2017.analytics.Analytics
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.schedule.repository.SessionsRepository
import de.droidcon.berlin2017.ui.lce.LceViewState
import de.droidcon.berlin2017.ui.lce.lceObservable
import io.reactivex.Observable
import timber.log.Timber

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SessionDetailsPresenter(
    private val sessionRepository: SessionsRepository,
    private val analytics: Analytics
) : MviBasePresenter<SessionDetailsView, LceViewState<Session>>() {

  override fun bindIntents() {
    //
    // No error handling nor snackbar info (added or removed to schedule). YOLO.
    //

    // Intent function takes care of unsubscribing
    intent(SessionDetailsView::clickOnFabIntent)
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

    val sessionData = intent(SessionDetailsView::loadIntent)
        .doOnNext { Timber.d("load Session intent. Session Id = $it") }
        .switchMap {
          // Will automatically update if Session is "favorite" / "not favorite anymore"
          lceObservable(sessionRepository.getSession(it))
        }

    subscribeViewState(sessionData, SessionDetailsView::render)
  }
}