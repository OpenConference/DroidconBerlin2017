package de.droidcon.berlin2017

import android.app.Application
import android.content.Context
import de.droidcon.berlin2017.di.ApplicationComponent
import de.droidcon.berlin2017.di.ApplicationModule
import de.droidcon.berlin2017.di.DaggerApplicationComponent
import de.droidcon.berlin2017.di.DaoModule
import de.droidcon.berlin2017.di.NetworkModule
import de.droidcon.berlin2017.di.RepositoriesModule
import de.droidcon.berlin2017.di.ScheduleModule
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 *
 *
 * @author Hannes Dorfmann
 */
open class DroidconApplication : Application() {

  private lateinit var applicationComponent: ApplicationComponent

  override fun onCreate() {
    super.onCreate()
    plantTimber()
    applicationComponent = applicationComponentBuilder().build()
  }

  open fun plantTimber() {
    Timber.plant(DebugTree())
  }

  open fun applicationComponentBuilder() =
      DaggerApplicationComponent.builder()
          .applicationModule(ApplicationModule(this))
          .daoModule(DaoModule(this))
          .networkModule(NetworkModule(this))
          .repositoriesModule(RepositoriesModule())
          .scheduleModule(ScheduleModule(this))

  companion object {
    fun getApplicationComponent(context: Context): ApplicationComponent {
      val app = context.applicationContext as DroidconApplication
      return app.applicationComponent
    }
  }
}