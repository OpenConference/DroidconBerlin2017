package de.droidcon.berlin2018.di

import android.content.Context
import dagger.Module
import dagger.Provides
import de.droidcon.berlin2018.notification.NotificationScheduler
import de.droidcon.berlin2018.schedule.backend.BackendScheduleAdapter
import de.droidcon.berlin2018.schedule.database.dao.LocationDao
import de.droidcon.berlin2018.schedule.database.dao.SessionDao
import de.droidcon.berlin2018.schedule.database.dao.SpeakerDao
import de.droidcon.berlin2018.schedule.sync.ScheduleSync
import de.droidcon.berlin2018.ui.twitter.TwitterInitializer

/**
 *
 *
 * @author Hannes Dorfmann
 */
@Module(
    includes = arrayOf(ScheduleModule::class, DaoModule::class, NetworkModule::class,
        RepositoriesModule::class,
        PicassoModule::class, NavigatorModule::class, ViewBindingModule::class,
        AnalyticsModule::class, ClockModule::class,
        SessionsModule::class, SearchModule::class, AppUpdateCheckerModule::class
    )
)
class ApplicationModule(c: Context) {

  private val applicationContext = c.applicationContext
  private val twitterInitializer = TwitterInitializer(applicationContext)

  @Provides
  fun provideScheduleSync(backend: BackendScheduleAdapter,
      notificationScheduler: NotificationScheduler,
      sessionDao: SessionDao,
      speakerDao: SpeakerDao,
      locationDao: LocationDao) = ScheduleSync(backend, notificationScheduler, sessionDao,
      speakerDao, locationDao)

  @Provides
  @ApplicationContext
  fun provideApplicationContext() = applicationContext

  @Provides
  fun provideTwitterInitializer() = twitterInitializer
}
