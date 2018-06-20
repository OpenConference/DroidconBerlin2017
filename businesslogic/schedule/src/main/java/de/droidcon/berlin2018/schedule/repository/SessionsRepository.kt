package de.droidcon.berlin2018.schedule.repository

import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.notification.NotificationScheduler
import de.droidcon.berlin2018.schedule.database.dao.SessionDao
import de.droidcon.berlin2018.schedule.sync.ScheduleDataAwareObservableFactory
import io.reactivex.Completable
import io.reactivex.Observable


/**
 * Responsible to load all sessions
 *
 * @author Hannes Dorfmann
 */
interface SessionsRepository {

  /**
   * Load all sessions
   */
  fun allSessions(): Observable<List<Session>>

  fun favoriteSessions(): Observable<List<Session>>

  fun getSession(id: String): Observable<Session>

  fun addSessionToSchedule(session: Session): Completable

  fun removeSessionFromSchedule(session: Session): Completable

  fun getSessionsOfSpeaker(speakerId: String): Observable<List<Session>>

  fun findSessionsWith(query: String): Observable<List<Session>>
}

/**
 * A [SessionsRepository] that uses the sessions from local database (synced with the backend)
 */
class LocalDbAndFirebaseRepository(
    private val scheduleDataAwareObservableFactory: ScheduleDataAwareObservableFactory,
    private val sessionDao: SessionDao,
    private val notificationScheduler: NotificationScheduler
) : SessionsRepository {

  override fun allSessions(): Observable<List<Session>> =
      scheduleDataAwareObservableFactory.create(sessionDao.getSessions())

  override fun favoriteSessions(): Observable<List<Session>> = scheduleDataAwareObservableFactory.create(
      sessionDao.getFavoriteSessions())

  override fun getSession(
      id: String): Observable<Session> = scheduleDataAwareObservableFactory.create(
      sessionDao.getById(id))

  override fun addSessionToSchedule(session: Session): Completable = sessionDao.setFavorite(
      session.id(),
      true).map { it > 0 }
      .flatMapCompletable {
        Completable.fromCallable { notificationScheduler.addOrRescheduleNotification(session) }
      }


  override fun removeSessionFromSchedule(
      session: Session): Completable = sessionDao.setFavorite(
      session.id(),
      false).map { it > 0 }
      .flatMapCompletable {
        Completable.fromCallable {
          notificationScheduler.removeNotification(session)
        }
      }

  override fun getSessionsOfSpeaker(speakerId: String): Observable<List<Session>> =
      scheduleDataAwareObservableFactory.create(sessionDao.getSessionsOfSpeaker(speakerId))

  override fun findSessionsWith(query: String): Observable<List<Session>> =
      scheduleDataAwareObservableFactory.create(sessionDao.findSessionsWith(query))
}
