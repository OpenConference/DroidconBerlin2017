package de.droidcon.berlin2018.di

import dagger.Module
import dagger.Provides
import de.droidcon.berlin2018.ui.sessions.PhoneSessionGrouper
import de.droidcon.berlin2018.ui.sessions.SessionGrouper

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
