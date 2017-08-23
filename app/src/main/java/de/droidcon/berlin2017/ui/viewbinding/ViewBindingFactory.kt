package de.droidcon.berlin2017.ui.viewbinding

import com.bluelinelabs.conductor.Controller

/**
 * A Factory that creates [ViewBinding] for a view controller
 *
 * @author Hannes Dorfmann
 */
class ViewBindingFactory(
    // We use java Class (to not have to add kotlin-reflect dependency) to the project#
    private val producers: Map<Class<out Controller>, () -> ViewBinding>
) {

  operator fun get(controller: Controller): ViewBinding {
    val producerFunc = producers[controller.javaClass] ?: throw NullPointerException(
        "No producer function registered for ${controller.javaClass}")
    return producerFunc()
  }
}