package de.droidcon.berlin2017.di

import dagger.Module
import dagger.Provides
import de.droidcon.berlin2017.clock.AndroidClock
import de.droidcon.berlin2017.clock.Clock

/**
 *
 *
 * @author Hannes Dorfmann
 */
@Module
class ClockModule {

  @Provides
  fun provideClock(): Clock = AndroidClock()
}