package de.droidcon.berlin2017.ui.splash

import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.SimpleColorFilter
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.splash.SplashView.ViewState
import de.droidcon.berlin2017.ui.viewbinding.LifecycleAwareRef
import de.droidcon.berlin2017.ui.viewbinding.ViewBinding
import io.reactivex.Observable
import timber.log.Timber

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SplashViewBinding() : ViewBinding(), SplashView {

  private var progressView: LottieAnimationView by LifecycleAwareRef(this)

  override fun bindView(rootView: View) {
    progressView = rootView.findViewById(R.id.loading)
    progressView.addColorFilterToLayer("green ring 1",
        SimpleColorFilter(progressView.resources.getColor(R.color.green)))
    progressView.addColorFilterToLayer("flamingo ring 3",
        SimpleColorFilter(progressView.resources.getColor(R.color.red)))
    progressView.addColorFilterToLayer("flamingo ring 2",
        SimpleColorFilter(progressView.resources.getColor(R.color.turkis)))
    progressView.addColorFilterToLayer("flamingo ring 1",
        SimpleColorFilter(progressView.resources.getColor(R.color.green_light)))
  }


  override fun loadIntent(): Observable<Unit> = Observable.just(Unit)

  override fun render(vs: ViewState) {
    Timber.d("Render $vs")


    /*
    when (vs) {
      is ViewState.Error -> navigator.showHome()
      is ViewState.LoadingProgress ->
        if (vs.isLoadingComplete()) navigator.showHome()
        else progressView.progress = vs.loadingProgress.toFloat() / 100f
    }
    */
  }
}