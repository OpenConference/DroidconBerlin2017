package de.droidcon.berlin2018.ui.splash

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2018.schedule.repository.SessionsRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit.SECONDS
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

    val data = intent(SplashView::loadIntent) // This will cause loading the data
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

        }

    val timer = Observable.timer(9, SECONDS) // Show splash screen at least 2 seconds

    val combined = Observable.combineLatest(listOf(data, timer)) {
      it[0] as SplashView.ViewState
    }.observeOn(AndroidSchedulers.mainThread())

    subscribeViewState(combined, SplashView::render)

  }
}
