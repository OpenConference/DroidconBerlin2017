package de.droidcon.berlin2017.di

import com.squareup.picasso.Picasso
import dagger.Component
import de.droidcon.berlin2017.analytics.Analytics
import de.droidcon.berlin2017.interactor.SessionsInteractor
import de.droidcon.berlin2017.schedule.backend.ScheduleDataStateDeterminer
import de.droidcon.berlin2017.schedule.repository.SessionsRepository
import de.droidcon.berlin2017.schedule.repository.SpeakerRepository
import de.droidcon.berlin2017.search.SearchEngine
import de.droidcon.berlin2017.ui.navigation.NavigatorFactory
import de.droidcon.berlin2017.ui.viewbinding.ViewBindingFactory

/**
 *
 *
 * @author Hannes Dorfmann
 */
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

  fun analytics() : Analytics
  fun scheduleStateDeterminer(): ScheduleDataStateDeterminer
  fun picasso(): Picasso
  fun sessionRepository(): SessionsRepository
  fun speakersRepository(): SpeakerRepository
  fun navigatorFactory(): NavigatorFactory
  fun uiBinderFactory(): ViewBindingFactory
  fun sessionsInteractor() : SessionsInteractor
  fun searchEngine() : SearchEngine
}