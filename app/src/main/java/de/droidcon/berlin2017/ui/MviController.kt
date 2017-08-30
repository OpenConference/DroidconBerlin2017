package de.droidcon.berlin2017.ui

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.hannesdorfmann.mosby3.MviConductorDelegateCallback
import com.hannesdorfmann.mosby3.MviConductorLifecycleListener
import com.hannesdorfmann.mosby3.mvi.MviPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import de.droidcon.berlin2017.analytics.AnalyticsLifecycleListener

/**
 * This is the base Presenter for Model-View-Intent based controllers.
 * By using this class you have to provide a [de.droidcon.berlin2017.ui.viewbinding.ViewBinding] through [de.droidcon.berlin2017.ui.viewbinding.ViewBindingFactory].
 * Furthermore, the [de.droidcon.berlin2017.ui.viewbinding.ViewBinding] must implement the View interface from MVI view.
 *
 * @author Hannes Dorfmann
 */

abstract class MviController<V : MvpView, P : MviPresenter<V, *>>(
    args: Bundle?
) : Controller(args), MviConductorDelegateCallback<V, P> {

  constructor() : this(null)


  private val mosbyDelegate by lazy { MviConductorLifecycleListener(this) }
  val navigator by lazy { applicationComponent().navigatorFactory()[this] }
  private val viewBinding by lazy { applicationComponent().uiBinderFactory()[this] }
  private var lifecycleListenersRegistered = false


  protected abstract val layoutRes: Int

  /**
   * Set to true if the Controller should be tracked automatically with analytics
   */
  protected open val trackingWithAnalytics = true


  @Suppress("UNCHECKED_CAST")
  override fun getMvpView(): V = viewBinding as V

  @CallSuper
  override fun onContextAvailable(context: Context) {
    super.onContextAvailable(context)

    // Because of dependency injection we can only register the components once a context
    // (used for depenedncy injection) is available

    if (!lifecycleListenersRegistered) {
      lifecycleListenersRegistered = true
      addLifecycleListener(viewBinding)
      addLifecycleListener(mosbyDelegate)
      if (trackingWithAnalytics)
        addLifecycleListener(AnalyticsLifecycleListener())
    }

  }

  override fun setRestoringViewState(restoringViewState: Boolean) {
    viewBinding.restoringViewState = restoringViewState
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
      inflater.inflate(layoutRes, container, false)

}