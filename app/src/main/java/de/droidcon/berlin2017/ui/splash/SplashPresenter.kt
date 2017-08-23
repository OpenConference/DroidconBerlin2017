package de.droidcon.berlin2017.ui.splash

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2017.schedule.repository.SessionsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
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
          sessionsRepository.allSessions()
              .take(1)
              .subscribeOn(Schedulers.io())
              .map { SplashView.ViewState.COMPLETED }
              .startWith(SplashView.ViewState.LOADING)
              .onErrorReturn {
                Timber.e(it)
                SplashView.ViewState.ERROR
              }
              .observeOn(AndroidSchedulers.mainThread())
        }


    subscribeViewState(observable, SplashView::render)

  }
}