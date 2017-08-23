package de.droidcon.berlin2017.di

import dagger.Module
import dagger.Provides
import de.droidcon.berlin2017.notification.NotificationScheduler
import de.droidcon.berlin2017.schedule.backend.ScheduleDataStateDeterminer
import de.droidcon.berlin2017.schedule.database.dao.SessionDao
import de.droidcon.berlin2017.schedule.database.dao.SpeakerDao
import de.droidcon.berlin2017.schedule.repository.LocalDbAndFirebaseRepository
import de.droidcon.berlin2017.schedule.repository.LocalDbAndFirebaseSpeakerRepository
import de.droidcon.berlin2017.schedule.repository.SessionsRepository
import de.droidcon.berlin2017.schedule.repository.SpeakerRepository
import de.droidcon.berlin2017.schedule.sync.ScheduleDataAwareObservableFactory
import de.droidcon.berlin2017.schedule.sync.ScheduleSync
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
      speakerDao: SpeakerDao): SpeakerRepository = LocalDbAndFirebaseSpeakerRepository(factory,
      speakerDao)

}