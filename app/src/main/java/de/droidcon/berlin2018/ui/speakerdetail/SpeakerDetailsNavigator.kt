package de.droidcon.berlin2018.ui.speakerdetail

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.model.Speaker
import de.droidcon.berlin2018.ui.navigation.Navigator
import de.droidcon.berlin2018.ui.sessiondetails.SessionDetailsController

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SpeakerDetailsNavigator(private val controller: Controller) : Navigator {

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
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSessionDetails(session: Session) {
    controller.router.pushController(
        RouterTransaction.with(
            SessionDetailsController(session.id())
        )
            .popChangeHandler(HorizontalChangeHandler())
            .pushChangeHandler(HorizontalChangeHandler())
    )
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
