package de.droidcon.berlin2017.schedule.repository

import de.droidcon.berlin2017.model.Speaker
import de.droidcon.berlin2017.schedule.database.dao.SpeakerDao
import de.droidcon.berlin2017.schedule.sync.ScheduleDataAwareObservableFactory
import io.reactivex.Observable

/**
 *
 *
 * @author Hannes Dorfmann
 */
interface SpeakerRepository {

  /**
   * Get a list of all speakers
   */
  fun allSpeakers(): Observable<List<Speaker>>

  fun getSpeaker(id: String): Observable<Speaker>

  /**
   * Find speakers that match the following criteria
   */
  fun findSessionsWith(query: String): Observable<List<Speaker>>
}

/**
 * A [SpeakerRepository] that uses the speakers from local database
 */
class LocalDbAndFirebaseSpeakerRepository(
    private val scheduleDataAwareObservableFactory: ScheduleDataAwareObservableFactory,
    private val speakerDao: SpeakerDao) : SpeakerRepository {

  override fun allSpeakers(): Observable<List<Speaker>> = scheduleDataAwareObservableFactory.create(
      speakerDao.getSpeakers())

  override fun getSpeaker(id: String): Observable<Speaker> =
      scheduleDataAwareObservableFactory.create(
          speakerDao.getSpeaker(id))

  override fun findSessionsWith(query: String): Observable<List<Speaker>> =
      scheduleDataAwareObservableFactory.create(speakerDao.findSessionsWith(query))
}