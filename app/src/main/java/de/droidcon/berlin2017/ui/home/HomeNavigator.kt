package de.droidcon.berlin2017.ui.home

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.model.Speaker
import de.droidcon.berlin2017.ui.myschedule.MyScheduleController
import de.droidcon.berlin2017.ui.navigation.Navigator
import de.droidcon.berlin2017.ui.search.SearchChangeHandler
import de.droidcon.berlin2017.ui.search.SearchController
import de.droidcon.berlin2017.ui.sessions.SessionsController
import de.droidcon.berlin2017.ui.speakers.SpeakersController

/**
 * The navigator for the home
 *
 * @author Hannes Dorfmann
 */
class HomeNavigator(private val controller : Controller) : Navigator {
  override fun showHome() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSessions() {
    controller.getChildRouter(controller.view!!.findViewById(R.id.home_controller_containers))
        .setRoot(
            RouterTransaction.with(SessionsController())
                .popChangeHandler(FadeChangeHandler())
                .pushChangeHandler(FadeChangeHandler())
        )
  }

  override fun showSearch() {
    val shared = controller.resources!!.getString(R.string.transition_searchbox)
    controller.router.pushController(
        RouterTransaction.with(SearchController())
            .popChangeHandler(SearchChangeHandler(listOf(shared)))
            .pushChangeHandler(SearchChangeHandler(listOf(shared)))
    )
  }

  override fun showMySchedule() {
    controller.getChildRouter(controller.view!!.findViewById(R.id.home_controller_containers))
        .setRoot(
            RouterTransaction.with(MyScheduleController())
                .popChangeHandler(FadeChangeHandler())
                .pushChangeHandler(FadeChangeHandler())
        )
  }

  override fun showSpeakers() {
   controller.getChildRouter(controller.view!!.findViewById(R.id.home_controller_containers))
       .setRoot(
           RouterTransaction.with(SpeakersController())
               .popChangeHandler(FadeChangeHandler())
               .pushChangeHandler(FadeChangeHandler())
       )
  }

  override fun showSpeakerDetails(speaker: Speaker) {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showSessionDetails(session: Session) {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showTweets() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun popSelfFromBackstack() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}