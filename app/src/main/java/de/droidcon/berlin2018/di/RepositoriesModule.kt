package de.droidcon.berlin2018.di

import dagger.Module
import dagger.Provides
import de.droidcon.berlin2018.notification.NotificationScheduler
import de.droidcon.berlin2018.schedule.backend.ScheduleDataStateDeterminer
import de.droidcon.berlin2018.schedule.database.dao.SessionDao
import de.droidcon.berlin2018.schedule.database.dao.SpeakerDao
import de.droidcon.berlin2018.schedule.repository.LocalDbAndFirebaseRepository
import de.droidcon.berlin2018.schedule.repository.LocalSpeakerRepository
import de.droidcon.berlin2018.schedule.repository.SessionsRepository
import de.droidcon.berlin2018.schedule.repository.SpeakerRepository
import de.droidcon.berlin2018.schedule.sync.ScheduleDataAwareObservableFactory
import de.droidcon.berlin2018.schedule.sync.ScheduleSync
import io.reactivex.schedulers.Schedulers

/**
 *
 *
 * @author Hannes Dorfmann
 */
@Module
class RepositoriesModule {

  @Provides
  fun providesScheduleDataAwareObservableFactory(scheduleSync: ScheduleSync,
      scheduleDataStateDeterminer: ScheduleDataStateDeterminer) =
      ScheduleDataAwareObservableFactory(scheduleSync, scheduleDataStateDeterminer, Schedulers.io())

  @Provides
  fun provideSessionRepository(factory: ScheduleDataAwareObservableFactory,
      sessionDao: SessionDao,
      notificationScheduler: NotificationScheduler): SessionsRepository = LocalDbAndFirebaseRepository(
      factory, sessionDao, notificationScheduler)

  @Provides
  fun provideSpeakerRepository(factory: ScheduleDataAwareObservableFactory,
      speakerDao: SpeakerDao): SpeakerRepository = LocalSpeakerRepository(factory,
      speakerDao)

}
