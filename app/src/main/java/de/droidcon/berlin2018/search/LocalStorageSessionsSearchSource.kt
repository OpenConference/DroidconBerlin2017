package de.droidcon.berlin2018.search

import de.droidcon.berlin2018.schedule.repository.SessionsRepository
import de.droidcon.berlin2018.ui.sessions.shortTime
import de.droidcon.berlin2018.ui.sessions.speakerNames
import io.reactivex.Observable
import org.threeten.bp.ZoneId


/**
 * Searches the local database for Sessions
 *
 * @author Hannes Dorfmann
 */
class LocalStorageSessionsSearchSource(
    private val sessionsRepository: SessionsRepository,
    private val zoneConferenceTakesPlace: ZoneId) : SearchSource {

  override fun search(
      query: String): Observable<List<SearchableItem>> =
      sessionsRepository.findSessionsWith(query)
          .map { it.map { SessionSearchableItem(
              session = it,
              speakers = it.speakers().speakerNames(),
              time = it.shortTime(zoneConferenceTakesPlace, true),
              location = it.locationName(),
              favorite = it.favorite()
          ) } }
}
