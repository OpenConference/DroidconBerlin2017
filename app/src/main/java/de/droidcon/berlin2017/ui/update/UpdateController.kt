package de.droidcon.berlin2017.ui.update

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.analytics.AnalyticsLifecycleListener
import de.droidcon.berlin2017.ui.applicationComponent


/**
 * Just displays the update dialog
 *
 * @author Hannes Dorfmann
 */
class UpdateController : Controller() {

  init {
    addLifecycleListener(AnalyticsLifecycleListener())
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    val v = inflater.inflate(R.layout.controller_dialog_new_update_available, container, false)

    val analytics = applicationComponent().analytics()

    v.findViewById<View>(R.id.close).setOnClickListener {
      analytics.trackInstallUpdateDismissed()
      router.popCurrentController()
    }
    v.findViewById<View>(R.id.install).setOnClickListener {
      analytics.trackInstallUpdateClicked()
      val packageName = applicationContext?.packageName ?: "de.droidcon.berlin2017"
      val uri = Uri.parse("http://play.google.com/store/apps/details?id=$packageName")// sending to Deal Website
      val i = Intent(Intent.ACTION_VIEW, uri)
      startActivity(i)
    }

    v.findViewById<View>(R.id.dialog_container).isClickable = true

    v.setOnClickListener { router.popCurrentController() }

    return v
  }
}