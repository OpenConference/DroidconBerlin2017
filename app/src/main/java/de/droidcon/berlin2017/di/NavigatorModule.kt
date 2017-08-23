package de.droidcon.berlin2017.di

import dagger.Module
import dagger.Provides
import de.droidcon.berlin2017.ui.navigation.NavigatorFactory
import de.droidcon.berlin2017.ui.splash.SplashController
import de.droidcon.berlin2017.ui.splash.SplashNavigator

/**
 *
 *
 * @author Hannes Dorfmann
 */
@Module
class NavigatorModule {

  @Provides
  fun provideNavigatorFactory() =
      NavigatorFactory(
          mapOf(
              SplashController::class.java to ::SplashNavigator
          )
      )

}