package de.droidcon.berlin2018.schedule.backend

import de.droidcon.berlin2018.model.Location
import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.model.Speaker
import io.reactivex.Single

/**
 * API to communicate with the backend. This is where you have to communicate with
 * your conferences backend
 * @author Hannes Dorfmann
 */
interface BackendScheduleAdapter {

  /**
   * Get a list of all Speakers
   */
  fun getSpeakers(): Single<BackendScheduleResponse<Speaker>>

  /**
   * Get a list of all locations where sessions will be heldt
   */
  fun getLocations(): Single<BackendScheduleResponse<Location>>

  /**
   * Get list of all sessions
   */
  fun getSessions(): Single<BackendScheduleResponse<Session>>
}


