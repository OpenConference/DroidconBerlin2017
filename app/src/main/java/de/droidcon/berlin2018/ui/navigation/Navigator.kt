package de.droidcon.berlin2018.ui.navigation

import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.model.Speaker

/**
 * Responsible to navigate through the app
 *
 * @author Hannes Dorfmann
 */
interface Navigator {

  fun showHome()

  fun showSessions()

  fun showMySchedule()

  fun showSpeakers()

  fun showSpeakerDetails(speaker: Speaker)

  fun showSessionDetails(session : Session)

  fun showTweets()

  fun showSearch()

  fun popSelfFromBackstack()

  fun showBarcamp() {}

  fun showLicences()
  fun showSourceCode()
}
