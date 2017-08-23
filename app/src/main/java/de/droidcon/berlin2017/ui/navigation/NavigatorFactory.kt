package de.droidcon.berlin2017.ui.navigation

import com.bluelinelabs.conductor.Controller

/**
 * A NavigatorFactory is responsible to create a [Navigator] for a given Controller
 *
 * @author Hannes Dorfmann
 */

class NavigatorFactory(
    // We use java Class (to not have to add kotlin-reflect dependency) to the project
    private val producers: Map<Class<out Controller>, (Controller) -> Navigator>
) {

  operator fun get(controller: Controller): Navigator {
    val producerFunc = producers[controller.javaClass] ?: throw NullPointerException(
        "No Navigator producers registered for ${controller.javaClass}")

    return producerFunc(controller)
  }
}