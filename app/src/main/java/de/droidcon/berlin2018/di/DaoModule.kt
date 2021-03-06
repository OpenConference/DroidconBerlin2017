package de.droidcon.berlin2018.di

import android.content.Context
import com.hannesdorfmann.sqlbrite.dao.DaoManager
import dagger.Module
import dagger.Provides
import de.droidcon.berlin2018.schedule.database.dao.LocationDao
import de.droidcon.berlin2018.schedule.database.dao.LocationDaoSqlite
import de.droidcon.berlin2018.schedule.database.dao.SessionDao
import de.droidcon.berlin2018.schedule.database.dao.SessionDaoSqlite
import de.droidcon.berlin2018.schedule.database.dao.SpeakerDao
import de.droidcon.berlin2018.schedule.database.dao.SpeakerDaoSqlite

@Module
class DaoModule(context: Context) {


  private val sessionDao: SessionDao
  private val speakerDao: SpeakerDao
  private val locationDao: LocationDao

  init {

    // DAO's
    sessionDao = SessionDaoSqlite()
    speakerDao = SpeakerDaoSqlite()
    locationDao = LocationDaoSqlite()
    DaoManager.with(context.applicationContext)
        .add(sessionDao)
        .add(speakerDao)
        .add(locationDao)
        .version(1)
        .databaseName("schedule.db")
        .build()
  }

  @Provides
  fun provideSessionDao() = sessionDao

  @Provides
  fun provideSpeakerDao() = speakerDao

  @Provides
  fun provideLocationDao() = locationDao

}
