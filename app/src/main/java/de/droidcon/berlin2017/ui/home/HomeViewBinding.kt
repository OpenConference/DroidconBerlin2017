package de.droidcon.berlin2017.ui.home

import android.support.design.widget.BottomNavigationView
import android.view.ViewGroup
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.disableShiftMode
import de.droidcon.berlin2017.ui.viewbinding.LifecycleAwareRef
import de.droidcon.berlin2017.ui.viewbinding.ViewBinding

/**
 * Binds the UI to [HomeController]
 *
 * @author Hannes Dorfmann
 */
class HomeViewBinding : ViewBinding(), HomeView {

  private var bottomNavigationView: BottomNavigationView by LifecycleAwareRef(this)

  private val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    when (item.itemId) {
      R.id.nav_sessions -> {
        navigator.showSessions()
        true
      }
      R.id.nav_my_schedule -> {
        navigator.showMySchedule()
        true
      }
      R.id.nav_speakers -> {
        navigator.showSpeakers()
        true
      }
      R.id.nav_twitter -> {
        navigator.showTweets()
        true
      }
      else -> false
    }
  }


  override fun bindView(rootView: ViewGroup) {
    bottomNavigationView = rootView.findViewById(R.id.navigation)
    bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener)
    bottomNavigationView.disableShiftMode()
    //bottomNavigationView.setOnNavigationItemReselectedListener { Timber.d("Selected $it")}
  }

}