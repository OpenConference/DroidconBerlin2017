package de.droidcon.berlin2017.analytics

import android.view.View
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Controller.LifecycleListener
import de.droidcon.berlin2017.ui.applicationComponent

/**
 * Lifecycle Listener to track the current visibile screen
 *
 * @author Hannes Dorfmann
 */
class AnalyticsLifecycleListener : LifecycleListener() {

  override fun postCreateView(controller: Controller, view: View) {
    controller.applicationComponent().analytics().trackScreen(controller)
  }
}