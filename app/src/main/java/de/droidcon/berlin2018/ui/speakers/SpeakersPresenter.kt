package de.droidcon.berlin2018.ui.speakers

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2018.model.Speaker
import de.droidcon.berlin2018.schedule.repository.SpeakerRepository
import de.droidcon.berlin2018.ui.lce.LceViewState
import de.droidcon.berlin2018.ui.lce.lceObservable
import timber.log.Timber

/**
 * Presenter for [SpeakersView]
 *
 * @author Hannes Dorfmann
 */
class SpeakersPresenter(
    private val speakerRepository: SpeakerRepository) : MviBasePresenter<SpeakersView, LceViewState<List<Speaker>>>() {

  override fun bindIntents() {
    val observable = intent(SpeakersView::loadIntent)
        .doOnNext { Timber.d("load Intent") }
        .switchMap { lceObservable(speakerRepository.allSpeakers()) }

    subscribeViewState(observable, SpeakersView::render)
  }
}
