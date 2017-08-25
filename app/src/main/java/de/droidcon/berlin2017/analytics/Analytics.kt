package de.droidcon.berlin2017.analytics

import com.bluelinelabs.conductor.Controller

/**
 * Analytics / Tracking
 *
 * @author Hannes Dorfmann
 */
interface Analytics {


  fun trackScreen(controller : Controller)
  fun trackSessionDetailsScreen(id: String)
  fun trackSpeakersDetailsScreen(id: String)
}