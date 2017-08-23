package de.droidcon.berlin2017.ui

import com.bluelinelabs.conductor.Controller
import de.droidcon.berlin2017.DroidconApplication
import de.droidcon.berlin2017.di.ApplicationComponent

/**
 * Get the application module
 *
 * @author Hannes Dorfmann
 */

fun Controller.applicationComponent(): ApplicationComponent {
  val context = applicationContext
  if (context == null)
    throw IllegalStateException(
        "Application Context is null because no activity attached to this controller. You are calling this outside of the regular controller lifecycle.")
  else return DroidconApplication.getApplicationComponent(context)
}
