package de.droidcon.berlin2017.di

import dagger.Component
import de.droidcon.berlin2017.schedule.repository.SessionsRepository
import javax.inject.Singleton

/**
 *
 *
 * @author Hannes Dorfmann
 */
@Component(modules = arrayOf(ApplicationModule::class))
@Singleton
interface ApplicationComponent {

  fun sessionRepository(): SessionsRepository

}