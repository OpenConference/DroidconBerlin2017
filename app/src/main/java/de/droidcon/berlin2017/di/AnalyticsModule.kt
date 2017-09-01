package de.droidcon.berlin2017.di

import android.content.Context
import dagger.Module
import dagger.Provides
import de.droidcon.berlin2017.BuildConfig
import de.droidcon.berlin2017.analytics.Analytics
import de.droidcon.berlin2017.analytics.FirebaseAnalytics
import de.droidcon.berlin2017.analytics.LoggingAnalytics

/**
 * Analytics Module
 *
 * @author Hannes Dorfmann
 */
@Module
class AnalyticsModule(context: Context) {
  private val analytics = if (BuildConfig.DEBUG) LoggingAnalytics() else FirebaseAnalytics(context)

  @Provides
  fun provideAnalytics(): Analytics = analytics
}