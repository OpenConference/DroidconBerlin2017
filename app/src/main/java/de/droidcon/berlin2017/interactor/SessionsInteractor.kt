package de.droidcon.berlin2017.interactor

import android.support.v7.util.DiffUtil
import de.droidcon.berlin2017.clock.Clock
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.schedule.repository.SessionsRepository
import de.droidcon.berlin2017.ui.lce.LceViewState
import de.droidcon.berlin2017.ui.lce.LceViewState.Content
import de.droidcon.berlin2017.ui.lce.lceObservable
import de.droidcon.berlin2017.ui.sessions.SchedulePresentationModel
import de.droidcon.berlin2017.ui.sessions.SessionGrouper
import de.droidcon.berlin2017.ui.sessions.Sessions
import io.reactivex.Observable
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

/**
 * Interactor that is responsible to connect various parts of business logic into one unit
 *
 * @author Hannes Dorfmann
 */
class SessionsInteractor @Inject constructor(
    private val sessionsRepository: SessionsRepository,
    private val clock: Clock,
    private val sessionGrouper: SessionGrouper
) {


  fun favoriteSessions(
      scrolledToNowIntent: Observable<Boolean>): Observable<LceViewState<Sessions>> =
      transform(scrolledToNowIntent, { sessionsRepository.favoriteSessions() })

  fun allSessions(scrolledToNowIntent: Observable<Boolean>): Observable<LceViewState<Sessions>> =
      transform(scrolledToNowIntent) { sessionsRepository.allSessions() }

  private inline fun transform(scrolledToNowIntent: Observable<Boolean>,
      crossinline dataSourceFactory: () -> Observable<List<Session>>): Observable<LceViewState<Sessions>> {

    val zoneConferenceTakesPlace = clock.getZoneConferenceTakesPlace()

    val dataObservable = dataSourceFactory().map { sessions ->
      sessionGrouper.groupSessions(sessions, zoneConferenceTakesPlace)
    }.map {
      Sessions(
          scrollTo = findScrollToIndex(it, clock.nowInConferenceTimeZone()),
          sessions = it,
          diffResult = null
      )
    }.scan { old: Sessions, new: Sessions ->

      new.copy(diffResult = DiffUtil.calculateDiff(ScheduleDiffCallback(old.sessions, new.sessions),
          true))

    }

    val observables = arrayOf(lceObservable(dataObservable), scrolledToNowIntent)

    return Observable.combineLatest(observables) {

      val lceState = it[0] as LceViewState<Sessions>
      val alreadyScrolledToNow = it[1] as Boolean
      if (lceState is Content) {
        if (alreadyScrolledToNow)
          Content(
              // We already have scrolled, so don't scroll again
              lceState.data.copy(scrollTo = null, sessions = lceState.data.sessions)
          )
        else
          lceState
      } else {
        lceState
      }

    }
        .distinctUntilChanged()
  }

  private fun findScrollToIndex(sessions: List<SchedulePresentationModel>,
      now: LocalDateTime): Int? {
    val position = sessions.binarySearch {
      when {
        it.date.isBefore(now) -> -1
        it.date.isAfter(now) -> 1
        else -> 0
      }
    }

    val toScroll = if (position >= 0)
      position
    else {
      val positionWhereItShouldBe = (-position - 1)
      Math.min(positionWhereItShouldBe, sessions.size - 1)
    }


    return Math.max(0,
        toScroll - 1) // -1 so that the real position is in the middle of the screen (not on top)
  }

}

private class ScheduleDiffCallback(val old: List<SchedulePresentationModel>,
    val new: List<SchedulePresentationModel>) : DiffUtil.Callback() {

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      old[oldItemPosition] == new[newItemPosition]

  override fun getOldListSize(): Int = old.size

  override fun getNewListSize(): Int = new.size

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      old[oldItemPosition] == new[newItemPosition]
}
