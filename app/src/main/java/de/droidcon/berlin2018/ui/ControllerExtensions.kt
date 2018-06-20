package de.droidcon.berlin2018.ui

import android.support.design.widget.BottomNavigationView
import com.android.support.design.internal.BottomNavigationViewHelper
import com.bluelinelabs.conductor.Controller
import de.droidcon.berlin2018.DroidconApplication
import de.droidcon.berlin2018.di.ApplicationComponent

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

fun BottomNavigationView.disableShiftMode() {
  BottomNavigationViewHelper.disableShiftMode(this)
}
