package de.droidcon.berlin2018.ui.home

import android.support.design.widget.BottomNavigationView
import android.view.ViewGroup
import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.ui.disableShiftMode
import de.droidcon.berlin2018.ui.searchbox.SearchBox
import de.droidcon.berlin2018.ui.viewbinding.LifecycleAwareRef
import de.droidcon.berlin2018.ui.viewbinding.ViewBinding

/**
 * Binds the UI to [HomeController]
 *
 * @author Hannes Dorfmann
 */
class HomeViewBinding : ViewBinding(), HomeView {

  private var bottomNavigationView: BottomNavigationView by LifecycleAwareRef(this)
  private var searchBox: SearchBox by LifecycleAwareRef(this)


  private val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    searchBox.slideInIfNeeded()
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
      R.id.nav_barcamp -> {
        navigator.showBarcamp()
        true
      }
      else -> false
    }
  }


  override fun bindView(rootView: ViewGroup) {
    searchBox = rootView.findViewById(R.id.searchBox)
    searchBox.showInput = false
    searchBox.setOnClickListener { navigator.showSearch() }
    searchBox.overflowMenuClickListener = {
      when (it) {
        0 -> navigator.showSourceCode()
        1 -> navigator.showLicences()
      }
    }

    bottomNavigationView = rootView.findViewById(R.id.navigation)
    bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener)
    bottomNavigationView.disableShiftMode()
    bottomNavigationView.setOnNavigationItemReselectedListener { } // Disallow reselect

  }

}
