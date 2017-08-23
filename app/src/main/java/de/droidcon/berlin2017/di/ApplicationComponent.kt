package de.droidcon.berlin2017.di

import com.squareup.picasso.Picasso
import dagger.Component
import de.droidcon.berlin2017.schedule.repository.SessionsRepository
import de.droidcon.berlin2017.ui.navigation.NavigatorFactory
import de.droidcon.berlin2017.ui.viewbinding.ViewBindingFactory

/**
 *
 *
 * @author Hannes Dorfmann
 */
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

  fun picasso() : Picasso
  fun sessionRepository(): SessionsRepository
  fun navigatorFactory(): NavigatorFactory
  fun uiBinderFactory(): ViewBindingFactory
}