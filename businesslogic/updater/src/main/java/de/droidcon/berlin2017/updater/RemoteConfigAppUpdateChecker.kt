package de.droidcon.berlin2017.updater

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

/**
 * Uses Firebase Remote config
 *
 * @author Hannes Dorfmann
 */
class RemoteConfigAppUpdateChecker(application: Application) : AppUpdateChecker {


  private val KEY_APP_VERSION = "latest_app_version"
  private val KEY_APP_PUBLISHED = "app_published"
  private val LOCAL_VERSION_NOT_AVAILABLE = -1L

  private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

  private val localAppVersion: Long = try {
    val pInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
    pInfo.versionCode.toLong()
  } catch (e: Throwable) {
    Timber.e(e)
    LOCAL_VERSION_NOT_AVAILABLE
  }


  private val appVersionSubject = BehaviorSubject.createDefault(AppVersion(
      newerAppVersionAvailable = false,
      appPublished = true
  ))

  init {

    // Default Settings
    remoteConfig.setDefaults(
        mapOf(
            KEY_APP_VERSION to localAppVersion,
            KEY_APP_PUBLISHED to true)
    )


    application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
      override fun onActivityResumed(p0: Activity?) {

        remoteConfig.fetch(30 * 60) // half an hour
            .addOnCompleteListener {
              if (it.isSuccessful) {
                remoteConfig.activateFetched()
                val latestRemoteVersion = remoteConfig.getLong(KEY_APP_VERSION)

                val newerVersion = localAppVersion != LOCAL_VERSION_NOT_AVAILABLE && latestRemoteVersion > localAppVersion
                val appPublished = remoteConfig.getBoolean(KEY_APP_PUBLISHED)

                appVersionSubject.onNext(AppVersion(
                    newerAppVersionAvailable = newerVersion,
                    appPublished = appPublished
                ))


              } else if (it.exception != null) {
                Timber.e(it.exception)
              }
            }

      }

      override fun onActivityPaused(p0: Activity?) {}
      override fun onActivityStarted(p0: Activity?) {}
      override fun onActivityDestroyed(p0: Activity?) {}
      override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {}
      override fun onActivityStopped(p0: Activity?) {}
      override fun onActivityCreated(p0: Activity?, p1: Bundle?) {}
    })
  }


  override fun newAppVersionAvailable(): Observable<AppVersion> = appVersionSubject.distinctUntilChanged()

}