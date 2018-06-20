package de.droidcon.berlin2018.ui.sessiondetails

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.model.Speaker
import de.droidcon.berlin2018.ui.navigation.Navigator
import de.droidcon.berlin2018.ui.speakerdetail.SpeakerDetailsController

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SessionDetailsNavigator(private val controller: Controller) : Navigator {

  override fun showHome() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSessions() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showMySchedule() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSpeakers() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSpeakerDetails(speaker: Speaker) {
    controller.router.pushController(
        RouterTransaction.with(
            SpeakerDetailsController(speaker.id())
        )
            .pushChangeHandler(HorizontalChangeHandler())
            .popChangeHandler(HorizontalChangeHandler())

    )
  }

  override fun showSessionDetails(session: Session) {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showTweets() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSearch() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun popSelfFromBackstack() {
    controller.router.popCurrentController()
  }


  override fun showLicences() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSourceCode() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
