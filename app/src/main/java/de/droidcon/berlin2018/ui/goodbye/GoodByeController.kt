package de.droidcon.berlin2018.ui.goodbye

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.analytics.AnalyticsLifecycleListener

/**
 * Shown once the conference is over
 *
 * @author Hannes Dorfmann
 */
class GoodByeController : Controller() {

  init {
    addLifecycleListener(AnalyticsLifecycleListener())
  }

  override fun onCreateView(inflater: LayoutInflater,
      container: ViewGroup): View = inflater.inflate(R.layout.controller_goodbye, container, false)
}
