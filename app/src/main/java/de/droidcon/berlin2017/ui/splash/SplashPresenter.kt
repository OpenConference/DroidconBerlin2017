package de.droidcon.berlin2017.ui.splash

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2017.schedule.repository.SessionsRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SplashPresenter @Inject constructor(
    private val sessionsRepository: SessionsRepository
) : MviBasePresenter<SplashView, SplashView.ViewState>() {

  override fun bindIntents() {

    val observable = intent(SplashView::loadIntent) // This will cause loading the data
        .doOnNext({ Timber.d("Intent: loading") })
        .switchMap {
          val loadingObservable: Observable<SplashView.ViewState> =
              sessionsRepository.allSessions()
                  .take(1)
                  .subscribeOn(Schedulers.io())
                  .map { SplashView.ViewState.LoadingProgress(100) as SplashView.ViewState }
                  .startWith(SplashView.ViewState.LoadingProgress(0))
                  .onErrorReturn { SplashView.ViewState.Error(it) }

          val progressAnimator = Observable.interval(200, MILLISECONDS)
              .take(99)
              .map {
                SplashView.ViewState.LoadingProgress(it.toInt())
              }

          loadingObservable.switchMap {
            when (it) {
              is SplashView.ViewState.Error -> Observable.just(it)
              is SplashView.ViewState.LoadingProgress ->
                if (!it.isLoadingComplete()) progressAnimator // Start animator
                else Observable.just(it) // Finished loading
            }
          }.observeOn(AndroidSchedulers.mainThread())
        }

    subscribeViewState(observable, SplashView::render)

  }
}