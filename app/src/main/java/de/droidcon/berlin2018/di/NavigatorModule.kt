package de.droidcon.berlin2018.di

import dagger.Module
import dagger.Provides
import de.droidcon.berlin2018.ui.barcamp.BarCampController
import de.droidcon.berlin2018.ui.barcamp.BarCampNavigator
import de.droidcon.berlin2018.ui.home.HomeController
import de.droidcon.berlin2018.ui.home.HomeNavigator
import de.droidcon.berlin2018.ui.myschedule.MyScheduleController
import de.droidcon.berlin2018.ui.myschedule.MyScheduleNavigator
import de.droidcon.berlin2018.ui.navigation.NavigatorFactory
import de.droidcon.berlin2018.ui.search.SearchController
import de.droidcon.berlin2018.ui.search.SearchViewNavigator
import de.droidcon.berlin2018.ui.sessiondetails.SessionDetailsController
import de.droidcon.berlin2018.ui.sessiondetails.SessionDetailsNavigator
import de.droidcon.berlin2018.ui.sessions.SessionsController
import de.droidcon.berlin2018.ui.sessions.SessionsNavigator
import de.droidcon.berlin2018.ui.speakerdetail.SpeakerDetailsController
import de.droidcon.berlin2018.ui.speakerdetail.SpeakerDetailsNavigator
import de.droidcon.berlin2018.ui.speakers.SpeakersController
import de.droidcon.berlin2018.ui.speakers.SpeakersNavigator
import de.droidcon.berlin2018.ui.splash.SplashController
import de.droidcon.berlin2018.ui.splash.SplashNavigator

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
              SplashController::class.java to ::SplashNavigator,
              HomeController::class.java to ::HomeNavigator,
              SpeakersController::class.java to ::SpeakersNavigator,
              SessionsController::class.java to ::SessionsNavigator,
              SearchController::class.java to ::SearchViewNavigator,
              SpeakerDetailsController::class.java to ::SpeakerDetailsNavigator,
              SessionDetailsController::class.java to ::SessionDetailsNavigator,
              MyScheduleController::class.java to ::MyScheduleNavigator,
              BarCampController::class.java to ::BarCampNavigator
          )
      )

}
