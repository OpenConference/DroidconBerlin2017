package de.droidcon.berlin2018.schedule.database.dao

import de.droidcon.berlin2018.model.Location
import io.reactivex.Observable


/**
 * Data-Access-Object for [com.droidcon.model.Location]
 *
 * @author Hannes Dorfmann
 */
interface LocationDao {

  /**
   * Get all locations
   */
  fun getLocations(): Observable<List<Location>>

  /**
   * Get a location by id
   */
  fun getById(id: String): Observable<Location>

  /**
   * Insert or update a location
   */
  fun insertOrUpdate(id: String, name: String): Observable<Long>

  /**
   * Removes a location
   */
  fun remove(id: String): Observable<Int>

  /**
   * Removes all locations
   */
  fun removeAll(): Observable<Int>
}
