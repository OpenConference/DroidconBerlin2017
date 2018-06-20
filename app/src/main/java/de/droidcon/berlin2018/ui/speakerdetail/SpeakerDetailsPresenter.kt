package de.droidcon.berlin2018.ui.speakerdetail

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2018.analytics.Analytics
import de.droidcon.berlin2018.interactor.SpeakerDetailsInteractor
import de.droidcon.berlin2018.ui.lce.LceViewState
import de.droidcon.berlin2018.ui.lce.lceObservable
import timber.log.Timber

/**
 * Presenter for [SpeakerDetailsView]
 *
 * @author Hannes Dorfmann
 */
class SpeakerDetailsPresenter(
    private val interactor: SpeakerDetailsInteractor,
    private val analytics: Analytics
) : MviBasePresenter<SpeakerDetailsView, LceViewState<SpeakerDetail>>() {

  override fun bindIntents() {
    val data = intent(SpeakerDetailsView::loadIntent)
        .doOnNext { Timber.d("Intent load speaker $it") }
        .doOnNext { analytics.trackLoadSpeakerDetails(it) }
        .switchMap {
          lceObservable(interactor.getSpeakerDetails(it))
        }

    subscribeViewState(data, SpeakerDetailsView::render)
  }


}
