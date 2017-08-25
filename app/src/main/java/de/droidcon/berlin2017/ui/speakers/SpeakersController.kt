package de.droidcon.berlin2017.ui.speakers

import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.MviController
import de.droidcon.berlin2017.ui.applicationComponent

/**
 * Displays a list of all speakers
 *
 * @author Hannes Dorfmann
 */
class SpeakersController : MviController<SpeakersView, SpeakersPresenter>() {

  override val layoutRes: Int = R.layout.controller_lce

  override fun createPresenter(): SpeakersPresenter = SpeakersPresenter(applicationComponent().speakersRepository())
}