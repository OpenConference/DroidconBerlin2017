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
  sealed class ViewState {

    protected val PROGRESS_RANGE: IntRange = 0..100

    data class LoadingProgress(val loadingProgress: Int) : ViewState() {
      init {
        require(PROGRESS_RANGE.contains(
            loadingProgress)) { "Progress must be between $PROGRESS_RANGE but was $loadingProgress" }
      }

      fun isLoadingComplete() = loadingProgress == 100
    }

    data class Error(val error: Throwable) : ViewState()
  }
}
