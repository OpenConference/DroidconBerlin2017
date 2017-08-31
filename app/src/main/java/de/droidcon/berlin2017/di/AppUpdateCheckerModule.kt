package de.droidcon.berlin2017.di

import android.app.Application
import dagger.Module
import dagger.Provides
import de.droidcon.berlin2017.updater.AppUpdateChecker
import de.droidcon.berlin2017.updater.RemoteConfigAppUpdateChecker

/**
 *
 *
 * @author Hannes Dorfmann
 */
@Module
class AppUpdateCheckerModule(application : Application) {
  private val updateChecker : AppUpdateChecker = RemoteConfigAppUpdateChecker(application)

  @Provides
  fun provideUpdateChecker() = updateChecker
}