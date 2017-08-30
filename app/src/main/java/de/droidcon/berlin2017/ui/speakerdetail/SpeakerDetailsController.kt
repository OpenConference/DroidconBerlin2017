package de.droidcon.berlin2017.ui.speakerdetail

import android.os.Bundle
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.MviController
import de.droidcon.berlin2017.ui.applicationComponent

/**
 *
 *
 * @author Hannes Dorfmann
 */

private fun createArgs(speakerId: String) : Bundle {
  val bundle = Bundle()
  bundle.putString(SpeakerDetailsController.KEY_SPEAKER_ID, speakerId)
  return bundle
}

class SpeakerDetailsController(args : Bundle?) : MviController<SpeakerDetailsView, SpeakerDetailsPresenter>(args) {

  constructor(speakerId: String) : this(createArgs(speakerId))

  companion object {
    val KEY_SPEAKER_ID = "SpeakerId"
  }

  override val layoutRes: Int = R.layout.controller_speaker_details

  override fun createPresenter(): SpeakerDetailsPresenter = SpeakerDetailsPresenter(applicationComponent().speakerDetailsInteractor())
}