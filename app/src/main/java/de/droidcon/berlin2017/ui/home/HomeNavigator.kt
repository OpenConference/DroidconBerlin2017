package de.droidcon.berlin2017.ui.home

import android.content.Intent
import android.net.Uri
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.model.Speaker
import de.droidcon.berlin2017.ui.applicationComponent
import de.droidcon.berlin2017.ui.barcamp.BarCampController
import de.droidcon.berlin2017.ui.myschedule.MyScheduleController
import de.droidcon.berlin2017.ui.navigation.Navigator
import de.droidcon.berlin2017.ui.search.SearchChangeHandler
import de.droidcon.berlin2017.ui.search.SearchController
import de.droidcon.berlin2017.ui.sessions.SessionsController
import de.droidcon.berlin2017.ui.speakers.SpeakersController
import de.droidcon.berlin2017.ui.twitter.TwitterController
import de.psdev.licensesdialog.LicensesDialog


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


  override fun showBarcamp() {
    controller.getChildRouter(controller.view!!.findViewById(R.id.home_controller_containers))
        .setRoot(
            RouterTransaction.with(BarCampController())
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
    controller.getChildRouter(controller.view!!.findViewById(R.id.home_controller_containers))
        .setRoot(
            RouterTransaction.with(TwitterController())
                .popChangeHandler(FadeChangeHandler())
                .pushChangeHandler(FadeChangeHandler())
        )
  }

  override fun popSelfFromBackstack() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showLicences() {
    controller.applicationComponent().analytics().trackShowLicenses()
    LicensesDialog.Builder(controller.activity!!)
        .setNotices(R.raw.notices)
        .setIncludeOwnLicense(true)
        .build()
        .show();
  }

  override fun showSourceCode() {
    controller.applicationComponent().analytics().trackShowSourceCode()
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("https://github.com/OpenConference/DroidconBerlin2017")
    controller.startActivity(intent)
  }
}