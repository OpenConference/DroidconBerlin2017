package de.droidcon.berlin2018.analytics

import android.content.Context
import android.os.Bundle
import com.bluelinelabs.conductor.Controller
import com.google.firebase.analytics.FirebaseAnalytics

/**
 *
 *
 * @author Hannes Dorfmann
 */
class FirebaseAnalytics(context: Context) : Analytics {

  private val firebase = FirebaseAnalytics.getInstance(context);

  override fun trackLoadSessionDetails(id: String) {
    val bundle = Bundle()
    bundle.putString("SessionId", id)
    firebase.logEvent("LoadSessionDetails", bundle)
  }

  override fun trackLoadSpeakerDetails(id: String) {
    val bundle = Bundle()
    bundle.putString("SpeakerId", id)
    firebase.logEvent("LoadSpeakerDetails", bundle)
  }


  override fun trackScreen(controller: Controller) {
    firebase.setCurrentScreen(controller.activity!!, controller::class.java.simpleName, null)
  }

  override fun trackSearch(query: String) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, query)
    firebase.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
  }

  override fun trackSessionMarkedAsFavorite(sessionId: String) {
    val bundle = Bundle()
    bundle.putString("SessionId", sessionId)
    firebase.logEvent("MarkSessionAsFavorite", bundle)
  }

  override fun trackSessionRemovedFromFavorite(sessionId: String) {
    val bundle = Bundle()
    bundle.putString("SessionId", sessionId)
    firebase.logEvent("RemoveSessionFromFavorite", bundle)
  }

  override fun trackInstallUpdateDismissed() {
    val bundle = Bundle()
    firebase.logEvent("InstallUpdateDismissed", bundle)
  }

  override fun trackInstallUpdateClicked() {
    val bundle = Bundle()
    firebase.logEvent("InstallUpdate", bundle)
  }

  override fun trackTwitterRefresh() {
    val bundle = Bundle()
    firebase.logEvent("TwitterPullToRefresh", bundle)
  }

  override fun trackSessionNotificationOpened(sessionId: String) {
    val bundle = Bundle()
    bundle.putString("SessionId", sessionId)
    firebase.logEvent("SessionNotificationOpened", bundle)
  }

  override fun trackSessionNotificationGenerated(sessionId: String) {
    val bundle = Bundle()
    bundle.putString("SessionId", sessionId)
    firebase.logEvent("SessionNotificationGenerated", bundle)
  }

  override fun trackShowSourceCode() {
    val bundle = Bundle()
    firebase.logEvent("ShowSourceCodeClicked", bundle)
  }

  override fun trackShowLicenses() {
    val bundle = Bundle()
    firebase.logEvent("ShowLicenseClicked", bundle)
  }

  override fun trackFastScrollStarted(controllerName: String) {
    val bundle = Bundle()
    bundle.putString("ControllerName", controllerName)
    firebase.logEvent("FastScrollStarted", bundle)
  }
}
