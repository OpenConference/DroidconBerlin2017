package de.droidcon.berlin2017.analytics

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

  override fun trackSessionDetailsScreen(id: String) {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun trackSpeakersDetailsScreen(id: String) {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }


  override fun trackScreen(controller: Controller) {
    firebase.setCurrentScreen(controller.activity!!, controller::class.java.simpleName, null)
  }

  override fun trackSearch(query: String) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, query)
    firebase.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
  }
}