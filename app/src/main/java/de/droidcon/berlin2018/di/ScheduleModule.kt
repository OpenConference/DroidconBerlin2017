package de.droidcon.berlin2018.di

import android.content.Context
import dagger.Module
import dagger.Provides
import de.droidcon.berlin2018.notification.DefaultNotificationScheduler
import de.droidcon.berlin2018.notification.NotificationReceiver
import de.droidcon.berlin2018.notification.NotificationScheduler
import de.droidcon.berlin2018.schedule.backend.ScheduleDataStateDeterminer
import de.droidcon.berlin2018.schedule.backend.TimebaseScheduleDataStateDeterminer

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
  fun provideScheduleDataStateDeterminer(): ScheduleDataStateDeterminer {
    val sharedPrefs = context.getSharedPreferences("TimebaseScheduleDeterminer",
        Context.MODE_PRIVATE)
    return TimebaseScheduleDataStateDeterminer(sharedPrefs)
  }

  @Provides
  fun provideNotificationScheduler(): NotificationScheduler = DefaultNotificationScheduler(context,
      { context, session ->
        NotificationReceiver.showNotificationIntent(context, session.id())
      })

}
