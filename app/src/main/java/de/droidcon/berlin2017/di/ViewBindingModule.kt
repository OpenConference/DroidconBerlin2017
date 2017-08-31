package de.droidcon.berlin2017.di

import dagger.Module
import dagger.Provides
import de.droidcon.berlin2017.ui.home.HomeController
import de.droidcon.berlin2017.ui.home.HomeViewBinding
import de.droidcon.berlin2017.ui.myschedule.MyScheduleController
import de.droidcon.berlin2017.ui.myschedule.MyScheduleViewBinding
import de.droidcon.berlin2017.ui.search.SearchController
import de.droidcon.berlin2017.ui.search.SearchViewBinding
import de.droidcon.berlin2017.ui.sessiondetails.SessionDetailsController
import de.droidcon.berlin2017.ui.sessiondetails.SessionDetailsViewBinding
import de.droidcon.berlin2017.ui.sessions.SessionsController
import de.droidcon.berlin2017.ui.sessions.SessionsViewBinding
import de.droidcon.berlin2017.ui.speakerdetail.SpeakerDetailsController
import de.droidcon.berlin2017.ui.speakerdetail.SpeakerDetailsViewBinding
import de.droidcon.berlin2017.ui.speakers.SpeakersController
import de.droidcon.berlin2017.ui.speakers.SpeakersViewBinding
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
          SplashController::class.java to ::SplashViewBinding,
          HomeController::class.java to ::HomeViewBinding,
          SpeakersController::class.java to ::SpeakersViewBinding,
          SessionsController::class.java to ::SessionsViewBinding,
          SearchController::class.java to ::SearchViewBinding,
          SpeakerDetailsController::class.java to ::SpeakerDetailsViewBinding,
          SessionDetailsController::class.java to ::SessionDetailsViewBinding,
          MyScheduleController::class.java to ::MyScheduleViewBinding
      )
  )
}