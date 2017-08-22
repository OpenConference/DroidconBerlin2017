package de.droidcon.berlin2017.di

import android.content.Context
import dagger.Module
import dagger.Provides
import de.droidcon.berlin2017.notification.DefaultNotificationScheduler
import de.droidcon.berlin2017.notification.NotificationScheduler
import de.droidcon.berlin2017.schedule.backend.ScheduleDataStateDeterminer
import de.droidcon.berlin2017.schedule.backend.TimebaseScheduleDataStateDeterminer
import de.droidcon.berlin2017.ui.MainActivity
import javax.inject.Singleton

/**
 *
 *
 * @author Hannes Dorfmann
 */
/**
 *
 * Provides some things related to the conferences schedule
 *
 * @author Hannes Dorfmann
 */
@Module
open class ScheduleModule(c: Context) {

  protected val context: Context = c.applicationContext

  @Provides
  @Singleton
  fun provideScheduleDataStateDeterminer(): ScheduleDataStateDeterminer {
    val sharedPrefs = context.getSharedPreferences("TimebaseScheduleDeterminer",
        Context.MODE_PRIVATE)
    return TimebaseScheduleDataStateDeterminer(sharedPrefs)
  }

  @Provides
  @Singleton
  fun provideNotificationScheduler(): NotificationScheduler = DefaultNotificationScheduler(context, { context, session -> MainActivity.buildSessionDetailsIntent(context, session)})
}