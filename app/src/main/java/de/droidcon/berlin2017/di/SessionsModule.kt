package de.droidcon.berlin2017.di

import dagger.Module
import dagger.Provides
import de.droidcon.berlin2017.ui.sessions.PhoneSessionGrouper
import de.droidcon.berlin2017.ui.sessions.SessionGrouper

/**
 * Defines some dependencies for displaying sessions in the app
 *
 * @author Hannes Dorfmann
 */
@Module
class SessionsModule {

  @Provides
  fun provideSessionGrouper(): SessionGrouper = PhoneSessionGrouper()
}
