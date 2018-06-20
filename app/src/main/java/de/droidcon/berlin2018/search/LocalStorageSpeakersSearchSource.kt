package de.droidcon.berlin2018.search

import de.droidcon.berlin2018.schedule.repository.SpeakerRepository
import io.reactivex.Observable

/**
 * Uses the local storage to search for speakers
 *
 * @author Hannes Dorfmann
 */
class LocalStorageSpeakersSearchSource(
    private val speakerRepository: SpeakerRepository) : SearchSource {

  override fun search(
      query: String): Observable<List<SearchableItem>> =
      speakerRepository.findSessionsWith(query)
          .map {
            it.map { SpeakerSearchableItem(it) }
          }

}
