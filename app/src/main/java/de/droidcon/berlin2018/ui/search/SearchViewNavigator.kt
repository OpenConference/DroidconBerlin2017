package de.droidcon.berlin2018.ui.search

import android.content.Intent
import android.net.Uri
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.model.Speaker
import de.droidcon.berlin2018.ui.applicationComponent
import de.droidcon.berlin2018.ui.navigation.Navigator
import de.droidcon.berlin2018.ui.sessiondetails.SessionDetailsController
import de.droidcon.berlin2018.ui.speakerdetail.SpeakerDetailsController
import de.psdev.licensesdialog.LicensesDialog

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SearchViewNavigator(private val controller: Controller) : Navigator {

  override fun showHome() {
    controller.router.popCurrentController()
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
        RouterTransaction.with(SpeakerDetailsController(speaker.id()))
            .popChangeHandler(HorizontalChangeHandler())
            .pushChangeHandler(HorizontalChangeHandler())
    )

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
