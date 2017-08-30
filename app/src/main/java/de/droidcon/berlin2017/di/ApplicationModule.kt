package de.droidcon.berlin2017.di

import android.content.Context
import dagger.Module
import dagger.Provides
import de.droidcon.berlin2017.notification.NotificationScheduler
import de.droidcon.berlin2017.schedule.backend.BackendScheduleAdapter
import de.droidcon.berlin2017.schedule.database.dao.LocationDao
import de.droidcon.berlin2017.schedule.database.dao.SessionDao
import de.droidcon.berlin2017.schedule.database.dao.SpeakerDao
import de.droidcon.berlin2017.schedule.sync.ScheduleSync

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
        SessionsModule::class, SearchModule::class)
)
class ApplicationModule(c: Context) {

  private val applicationContext = c.applicationContext

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
}