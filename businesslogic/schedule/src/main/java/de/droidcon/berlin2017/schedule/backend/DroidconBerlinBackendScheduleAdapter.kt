package de.droidcon.berlin2017.schedule.backend

import de.droidcon.berlin2017.model.Location
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.model.Speaker
import io.reactivex.Single

/**
 * [BackendScheduleAdapter] for droidcon Berlin backend
 *
 * @author Hannes Dorfmann
 */
public class DroidconBerlinBackendScheduleAdapter(
    private val backend: DroidconBerlinBackend) : BackendScheduleAdapter {

  override fun getSpeakers(): Single<BackendScheduleResponse<Speaker>> =
      backend.getSpeakers().map {
        BackendScheduleResponse.dataChanged(it as List<Speaker>)
      }

  override fun getLocations(): Single<BackendScheduleResponse<Location>>
      = backend.getLocations().map {
    BackendScheduleResponse.dataChanged(it as List<Location>)
  }

  override fun getSessions(): Single<BackendScheduleResponse<Session>> =
      backend.getSessions().map {
        BackendScheduleResponse.dataChanged(it as List<Session>)
      }
}