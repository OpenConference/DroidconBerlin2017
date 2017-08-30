package de.droidcon.berlin2017.ui.speakerdetail

import com.hannesdorfmann.mosby3.mvp.MvpView
import de.droidcon.berlin2017.ui.lce.LceViewState
import io.reactivex.Observable



/**
 * Dispalys the details about a speaker
 *
 * @author Hannes Dorfmann
 */
interface SpeakerDetailsView : MvpView {

  /**
   * emits the id of the speaker
   */
  fun loadIntent(): Observable<String>

  fun render(state: LceViewState<SpeakerDetail>)
}