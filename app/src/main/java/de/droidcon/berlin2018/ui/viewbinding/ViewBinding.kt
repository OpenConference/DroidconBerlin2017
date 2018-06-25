package de.droidcon.berlin2018.ui.viewbinding

import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Controller.LifecycleListener
import com.squareup.picasso.Picasso
import de.droidcon.berlin2018.ui.MviController
import de.droidcon.berlin2018.ui.applicationComponent
import de.droidcon.berlin2018.ui.navigation.Navigator

/**
 * A ViewBinder is the middle man component between a controller and  the real UI widgets.
 * Hence a [Controller] has no direct reference to a UI widget but is rather just the Lifecycle Container.
 *
 *
 * To avoid memory leaks a [ViewBinding] is lifecycle aware of Controller's View Lifecycle namely
 * [Controller.onCreateView] and [Controller.onDestroyView]
 *
 * @author Hannes Dorfmann
 */
abstract class ViewBinding : LifecycleListener(), LifecycleOwner {

  lateinit protected var picasso: Picasso
  lateinit protected var navigator: Navigator
  lateinit protected var controller: Controller
  var restoringViewState = false
  private val clearCallbacks = ArrayList<() -> Unit>()

  override fun postCreateView(controller: Controller, view: View) {
    this.controller = controller
    picasso = controller.applicationComponent().picasso()
    navigator = (controller as MviController<*, *>).navigator
    bindView(view as ViewGroup)
  }

  override fun postDestroyView(controller: Controller) {
    for (callback in clearCallbacks) {
      callback()
    }
  }

  protected abstract fun bindView(rootView: ViewGroup)

  override fun addListener(clearCallback: () -> Unit) {
    clearCallbacks.add(clearCallback)
  }
}
