package de.droidcon.berlin2017.di

import dagger.Module
import dagger.Provides
import de.droidcon.berlin2017.ui.splash.SplashController
import de.droidcon.berlin2017.ui.splash.SplashViewBinding
import de.droidcon.berlin2017.ui.viewbinding.ViewBindingFactory

/**
 * Dagger module for [de.droidcon.berlin2017.ui.viewbinding.ViewBinding]
 *
 * @author Hannes Dorfmann
 */
@Module
class ViewBindingModule {
  @Provides
  fun provideViewBindingFactory() = ViewBindingFactory(
      mapOf(
          SplashController::class.java to ::SplashViewBinding
      )
  )
}