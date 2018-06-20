package de.droidcon.berlin2018.schedule.database.dao

import de.droidcon.berlin2018.model.Speaker
import io.reactivex.Observable


/**
 * Data-Access-Object for [com.droidcon.model.Speaker]
 *
 * @author Hannes Dorfmann
 */
interface SpeakerDao {

  /**
   * Insert or update a given Speaker
   */
  fun insertOrUpdate(id: String, name: String,
      info: String?, profilePicUrl: String?, company: String?,
      jobTitle: String?, link1: String?, link2: String?, link3: String?): Observable<Long>

  /**
   * Remove a speaker
   * @return Observable containing the number of deleted speakers (should be 1 or 0)
   */
  fun remove(id: String): Observable<Int>

  /**
   * Removes all speakers
   * @return Observable containing the number of deleted speakers
   */
  fun removeAll(): Observable<Int>

  /**
   * Get a given Speaker by his id
   */
  fun getSpeaker(id: String): Observable<Speaker>

  /**
   * Get all speakers
   */
  fun getSpeakers(): Observable<List<Speaker>>

  /**
   * Find speakers that match the following criteria
   */
  fun findSessionsWith(query: String): Observable<List<Speaker>>
}
