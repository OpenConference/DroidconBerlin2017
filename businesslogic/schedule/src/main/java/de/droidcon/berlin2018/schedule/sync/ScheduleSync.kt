package de.droidcon.berlin2018.schedule.sync

import de.droidcon.berlin2018.model.Location
import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.model.Speaker
import de.droidcon.berlin2018.notification.AddOrRescheduleNotificationCommand
import de.droidcon.berlin2018.notification.NotificationScheduler
import de.droidcon.berlin2018.notification.NotificationSchedulerCommand
import de.droidcon.berlin2018.notification.RemoveScheduledNotificationCommand
import de.droidcon.berlin2018.schedule.backend.BackendScheduleAdapter
import de.droidcon.berlin2018.schedule.backend.BackendScheduleResponse
import de.droidcon.berlin2018.schedule.database.dao.LocationDao
import de.droidcon.berlin2018.schedule.database.dao.SessionDao
import de.droidcon.berlin2018.schedule.database.dao.SpeakerDao
import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber
import java.util.ArrayList
import java.util.HashMap

/**
 * Responsible to sync the local database with your conferences backend
 * @author Hannes Dorfmann
 */
class ScheduleSync(
    private val backend: BackendScheduleAdapter,
    private val notificationScheduler: NotificationScheduler,
    private val sessionDao: SessionDao,
    private val speakerDao: SpeakerDao,
    private val locationDao: LocationDao) {


  /**
   * Loads the data from backend and stores it into the local persistent storage for offline support
   */
  fun executeSync(): Completable {

    val zipped: Single<Boolean> = Single.zip(listOf(backend.getLocations(),
        backend.getSpeakers(), backend.getSessions())) { results ->

      Timber.d("Sync executed, results received")
      val locationsResponse = results[0] as BackendScheduleResponse<Location>
      val speakersResponse = results[1] as BackendScheduleResponse<Speaker>
      val sessionsResponse = results[2] as BackendScheduleResponse<Session>

      if (locationsResponse.isNewerDataAvailable
          || speakersResponse.isNewerDataAvailable
          || sessionsResponse.isNewerDataAvailable) {

        // At least one data has been changed, so update local database
        // TODO move all sessions into transaction
        val allSessions: List<Session> =
            sessionDao.getSessions()
                .blockingFirst()


        val transaction = sessionDao.getBriteDatabase().newTransaction()
        try {

          // Update locations
          if (locationsResponse.isNewerDataAvailable) {
            locationDao.removeAll().blockingFirst()
            locationsResponse.data.forEach {
              locationDao.insertOrUpdate(it.id(), it.name()).blockingFirst()
            }
          }

          // Update Speakers
          if (speakersResponse.isNewerDataAvailable) {
            speakerDao.removeAll().blockingFirst()
            speakersResponse.data.forEach {
              speakerDao.insertOrUpdate(it.id(), it.name(), it.info(), it.profilePic(),
                  it.company(),
                  it.jobTitle(), it.link1(), it.link2(), it.link3()).blockingFirst()
            }
          }

          val notificaionSchedulerCommands = ArrayList<NotificationSchedulerCommand>()
          // Update Sessions
          if (sessionsResponse.isNewerDataAvailable) {

            val newSessionData: HashMap<String, Session> = sessionsResponse
                .data.fold(HashMap<String, Session>(), { map, session ->
              map.put(session.id(), session)
              map
            })

            allSessions.forEach {

              val newSession = newSessionData[it.id()]

              if (newSession == null) {
                // Session has been removed
                sessionDao.remove(it.id()).blockingFirst()
                if (it.favorite()) {
                  notificaionSchedulerCommands.add(
                      RemoveScheduledNotificationCommand(it, notificationScheduler))
                }
              } else {
                // check if time changed
                if (it.favorite() && it.startTime() != newSession.startTime()) {
                  notificaionSchedulerCommands.add(
                      AddOrRescheduleNotificationCommand(newSession, notificationScheduler))
                }

                sessionDao.insertOrUpdate(newSession, it.favorite()).blockingFirst()

                // Handled session, so can be removed
                newSessionData.remove(newSession.id())
              }
            }

            // Contains only unhandled values, so then new Sessions should be added
            newSessionData.values.forEach {
              sessionDao.insertOrUpdate(it, false).blockingFirst()
            }
          }

          transaction.markSuccessful()
          notificaionSchedulerCommands.forEach { it.execute() } // Execute only if transaction was successful
          Timber.d("Synced successfully")
        } finally {
          transaction.end()
        }

      }

      true
    }

    return zipped.toCompletable()
  }

}
