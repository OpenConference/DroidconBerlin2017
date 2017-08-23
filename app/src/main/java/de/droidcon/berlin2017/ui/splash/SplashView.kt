package de.droidcon.berlin2017.ui.splash

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable


/**
 * View for Splash screen
 *
 * @author Hannes Dorfmann
 */
interface SplashView : MvpView {

  /**
   * Intent to load the data
   */
  fun loadIntent(): Observable<Unit>

  /**
   * render state in View
   */
  fun render(vs: ViewState)


  /**
   * States a View can render
   */
  enum class ViewState {
    LOADING,
    ERROR,
    COMPLETED
  }
}
