package de.droidcon.berlin2018.di

import dagger.Module
import dagger.Provides
import de.droidcon.berlin2018.clock.Clock
import de.droidcon.berlin2018.schedule.repository.SessionsRepository
import de.droidcon.berlin2018.schedule.repository.SpeakerRepository
import de.droidcon.berlin2018.search.DefaultSearchEngine
import de.droidcon.berlin2018.search.LocalStorageSessionsSearchSource
import de.droidcon.berlin2018.search.LocalStorageSpeakersSearchSource
import de.droidcon.berlin2018.search.SearchEngine

/**
 *
 *
 * @author Hannes Dorfmann
 */
@Module
class SearchModule {


  @Provides
  fun provideSearchEngine(sessionsRepository: SessionsRepository,
      speakerRepository: SpeakerRepository,
      clock : Clock): SearchEngine = DefaultSearchEngine(
      listOf(
          LocalStorageSessionsSearchSource(sessionsRepository, clock.getZoneConferenceTakesPlace()),
          LocalStorageSpeakersSearchSource(speakerRepository)
      )
  )
}
