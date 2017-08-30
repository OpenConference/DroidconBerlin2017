package de.droidcon.berlin2017.ui.splash

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.model.Speaker
import de.droidcon.berlin2017.ui.home.HomeController
import de.droidcon.berlin2017.ui.navigation.Navigator

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SplashNavigator(private val controller: Controller) : Navigator {

  override fun showHome() {
    controller.router.setRoot(RouterTransaction.with(HomeController()).pushChangeHandler(FadeChangeHandler()).popChangeHandler(FadeChangeHandler()))
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
    TODO(
        "not implemented")
  }

  override fun showTweets() {
    TODO(
        "not implemented")
  }

  override fun showSearch() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun popSelfFromBackstack() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}