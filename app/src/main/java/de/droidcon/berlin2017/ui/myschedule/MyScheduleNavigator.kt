package de.droidcon.berlin2017.ui.myschedule

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.model.Speaker
import de.droidcon.berlin2017.ui.navigation.Navigator
import de.droidcon.berlin2017.ui.sessiondetails.SessionDetailsController
import de.droidcon.berlin2017.ui.sessions.SessionsController

/**
 *
 *
 * @author Hannes Dorfmann
 */
class MyScheduleNavigator(private val controller: Controller) : Navigator {

  override fun showHome() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSessions() {
    controller.router.pushController(
        RouterTransaction.with(SessionsController())
            .popChangeHandler(FadeChangeHandler())
            .pushChangeHandler(FadeChangeHandler())
    )
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
    controller.parentController!!.router.pushController(
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
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
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