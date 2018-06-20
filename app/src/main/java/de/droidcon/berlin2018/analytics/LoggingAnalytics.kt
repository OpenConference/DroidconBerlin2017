package de.droidcon.berlin2018.analytics

import android.util.Log
import com.bluelinelabs.conductor.Controller

/**
 *
 *
 * @author Hannes Dorfmann
 */
class LoggingAnalytics : Analytics {
  private val TAG = "Analytics"
  override fun trackScreen(controller: Controller) {
    Log.d(TAG, "track screen " + controller::class.java)
  }

  override fun trackLoadSessionDetails(id: String) {
    Log.d(TAG, "track load session details $id")
  }

  override fun trackLoadSpeakerDetails(id: String) {

    Log.d(TAG, "track load speaker details $id")
  }

  override fun trackSearch(query: String) {

    Log.d(TAG, "track searching for $query")
  }

  override fun trackSessionMarkedAsFavorite(sessionId: String) {

    Log.d(TAG, "mark as favorite $sessionId")
  }

  override fun trackSessionRemovedFromFavorite(sessionId: String) {
    Log.d(TAG, "remove session as favorie $sessionId")
  }

  override fun trackInstallUpdateDismissed() {
    Log.d(TAG, "NOT installing update. Dismissed!")
  }

  override fun trackInstallUpdateClicked() {
    Log.d(TAG, "Install update")
  }

  override fun trackTwitterRefresh() {
    Log.d(TAG, "twitter pull to refresh")
  }

  override fun trackSessionNotificationOpened(sessionId: String) {
    Log.d(TAG, "notification opened $sessionId")
  }

  override fun trackSessionNotificationGenerated(sessionId: String) {
    Log.d(TAG, "notification generated for $sessionId")
  }

  override fun trackShowSourceCode() {
    Log.d(TAG, "show source code")
  }

  override fun trackShowLicenses() {
    Log.d(TAG, "show license")
  }

  override fun trackFastScrollStarted(controllerName: String) {
    Log.d(TAG, "fast scroll in $controllerName")
  }
}
