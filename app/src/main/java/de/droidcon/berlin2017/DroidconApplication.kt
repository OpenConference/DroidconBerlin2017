package de.droidcon.berlin2017

import android.content.Context
import android.support.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import de.droidcon.berlin2017.di.AnalyticsModule
import de.droidcon.berlin2017.di.AppUpdateCheckerModule
import de.droidcon.berlin2017.di.ApplicationComponent
import de.droidcon.berlin2017.di.ApplicationModule
import de.droidcon.berlin2017.di.DaggerApplicationComponent
import de.droidcon.berlin2017.di.DaoModule
import de.droidcon.berlin2017.di.NavigatorModule
import de.droidcon.berlin2017.di.NetworkModule
import de.droidcon.berlin2017.di.PicassoModule
import de.droidcon.berlin2017.di.RepositoriesModule
import de.droidcon.berlin2017.di.ScheduleModule
import de.droidcon.berlin2017.di.ViewBindingModule
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 *
 *
 * @author Hannes Dorfmann
 */
open class DroidconApplication : MultiDexApplication() {

  private lateinit var applicationComponent: ApplicationComponent

  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
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
          .picassoModule(PicassoModule(this))
          .analyticsModule(AnalyticsModule(this))
          .navigatorModule(NavigatorModule())
          .viewBindingModule(ViewBindingModule())
          .appUpdateCheckerModule(AppUpdateCheckerModule(this))

  companion object {
    fun getApplicationComponent(context: Context): ApplicationComponent {
      val app = context.applicationContext as DroidconApplication
      return app.applicationComponent
    }
  }
}