package de.droidcon.berlin2018.interactor

import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.model.Speaker
import de.droidcon.berlin2018.schedule.repository.SessionsRepository
import de.droidcon.berlin2018.schedule.repository.SpeakerRepository
import de.droidcon.berlin2018.ui.speakerdetail.SpeakerBioItem
import de.droidcon.berlin2018.ui.speakerdetail.SpeakerDetail
import de.droidcon.berlin2018.ui.speakerdetail.SpeakerDetailsItem
import de.droidcon.berlin2018.ui.speakerdetail.SpeakerJobInfoItem
import de.droidcon.berlin2018.ui.speakerdetail.SpeakerLinkItem
import de.droidcon.berlin2018.ui.speakerdetail.SpeakerSessionItem
import io.reactivex.Observable
import java.util.ArrayList
import javax.inject.Inject

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SpeakerDetailsInteractor @Inject constructor(
    private val sessionsRepository: SessionsRepository,
    private val speakerRepository: SpeakerRepository
) {

  fun getSpeakerDetails(speakerId: String): Observable<SpeakerDetail> {

    val observables = arrayOf(speakerRepository.getSpeaker(speakerId),
        sessionsRepository.getSessionsOfSpeaker(speakerId))

    return Observable.combineLatest(observables) {
      val speaker = it[0] as Speaker
      val sessions = it[1] as List<Session>

      toPresentationModel(speaker, sessions)
    }

  }


  private fun toPresentationModel(speaker: Speaker, sessions: List<Session>): SpeakerDetail {

    val items = ArrayList<SpeakerDetailsItem>()

    // Bio
    val bio = speaker.info()
    if (bio != null) {
      items.add(SpeakerBioItem(bio))
    }


    val company = speaker.company()
    val jobTitle = speaker.jobTitle()

    // Job Info
    if (company != null || jobTitle != null) {
      items.add(SpeakerJobInfoItem(company, jobTitle))
    }


    // Links
    var firstLink = true
    val link1 = speaker.link1()
    if (link1 != null) {
      items.add(SpeakerLinkItem(link1, firstLink))
      firstLink = false
    }

    val link2 = speaker.link2()
    if (link2 != null) {
      items.add(SpeakerLinkItem(link2, firstLink))
      firstLink = false
    }

    val link3 = speaker.link3()
    if (link3 != null) {
      items.add(SpeakerLinkItem(link3, firstLink))
      firstLink = false
    }

    // Sessions
    var firstSession = true
    sessions.filter { it.title() != null }.forEach {
      items.add(SpeakerSessionItem(it, firstSession))
      firstSession = false
    }

    return SpeakerDetail(speaker.name(), speaker.profilePic(), items)
  }
}
