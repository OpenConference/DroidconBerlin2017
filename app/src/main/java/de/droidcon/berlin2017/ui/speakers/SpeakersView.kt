package de.droidcon.berlin2017.ui.speakers

import com.hannesdorfmann.mosby3.mvp.MvpView
import de.droidcon.berlin2017.model.Speaker
import de.droidcon.berlin2017.ui.lce.LceViewState
import io.reactivex.Observable

/**
 * View to display a list of all speakers
 *
 * @author Hannes Dorfmann
 */
interface SpeakersView : MvpView{

  fun loadIntent(): Observable<Unit>

  fun render(state: LceViewState<List<Speaker>>)


}