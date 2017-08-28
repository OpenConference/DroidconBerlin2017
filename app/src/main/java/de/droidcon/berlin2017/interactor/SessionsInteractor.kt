package de.droidcon.berlin2017.interactor

import de.droidcon.berlin2017.clock.Clock
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

  fun allSessions(scrolledToNowIntent: Observable<Boolean>): Observable<LceViewState<Sessions>> {

    val zoneConferenceTakesPlace = clock.getZoneConferenceTakesPlace()

    val dataObservable = sessionsRepository.allSessions().map { sessions ->
      sessionGrouper.groupSessions(sessions, zoneConferenceTakesPlace)
    }.map {
      Sessions(
          scrollTo = findScrollToIndex(it, clock.nowInConferenceTimeZone()),
          sessions = it
      )
    }

    val observables = arrayOf(lceObservable(dataObservable), scrolledToNowIntent)

    return Observable.combineLatest(observables) {

      val lceState = it[0] as LceViewState<Sessions>
      val scrolledToNow = it[1] as Boolean
      if (lceState is Content) {
        if (scrolledToNow)
          Content(
              // We already have scrolled, so don't scroll again
              lceState.data.copy(scrollTo = null, sessions = lceState.data.sessions)
          )
        else
          lceState
      } else {
        lceState
      }

    }.distinct()
  }

  private fun findScrollToIndex(sessions: List<SchedulePresentationModel>,
      now: LocalDateTime): Int? {
    // binary search
    // Assumption: List is sorted by date
    if (sessions.isEmpty())
      return null

    return binarySearchStartTime(low = 0, high = sessions.size - 1, sessions = sessions, now = now)
  }

  private fun binarySearchStartTime(low: Int, high: Int, sessions: List<SchedulePresentationModel>,
      now: LocalDateTime): Int? {

    val diff = (high - low) / 2

    if (low == 0 && diff == 0)
      return null // Don't scroll is the same as scroll to 0 position

    if (high == sessions.size && diff == 0)
      return sessions.size - 1


    val middle = low + diff
    val middleStartTime = sessions[middle].date

    if (now == middleStartTime) {
      return middle
    }


    if (now.isBefore(middleStartTime)) {
      val beforeMiddle = middle - 1
      if (beforeMiddle >= 0) {
        val beforeMiddleStartTime = sessions[beforeMiddle].date
        if (now.isAfter(beforeMiddleStartTime)) {
          // now is between session[middle-1] and session[middle]
          return middle
        }
      }
      return binarySearchStartTime(low = low, high = middle, sessions = sessions, now = now)
    } else {
      val afterMiddle = middle + 1
      if (afterMiddle < sessions.size) {
        val afterMiddleStartTime = sessions[afterMiddle].date
        if (now.isBefore(afterMiddleStartTime)) {
          // now is between session[middle] and session[middle + 1]
          return afterMiddle
        }
      }
      return binarySearchStartTime(low = middle, high = high, sessions = sessions, now = now)
    }
  }

}
