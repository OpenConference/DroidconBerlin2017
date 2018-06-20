package de.droidcon.berlin2018.ui.viewbinding

import kotlin.reflect.KProperty

interface LifecycleOwner {

  fun addListener(clearCallback: () -> Unit)
}

/**
 * This is a reference to a ui widget
 *
 * @author Hannes Dorfmann
 */
class LifecycleAwareRef<T>(lifecycleOwner: LifecycleOwner) {

  init {
    lifecycleOwner.addListener({ realRef = null })
  }

  private var realRef: T? = null


  operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
    return realRef ?: throw IllegalStateException(
        "View reference has been cleared. Seems that you call this method outside of the controllers lifecycle.")
  }


  operator fun setValue(thisRef: Any?, property: KProperty<*>?, value: T) {
    realRef = value
  }


}
