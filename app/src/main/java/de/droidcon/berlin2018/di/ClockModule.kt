package de.droidcon.berlin2018.di

import dagger.Module
import dagger.Provides
import de.droidcon.berlin2018.clock.AndroidClock
import de.droidcon.berlin2018.clock.Clock

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
