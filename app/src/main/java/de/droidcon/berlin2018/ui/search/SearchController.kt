package de.droidcon.berlin2018.ui.search

import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.ui.MviController
import de.droidcon.berlin2018.ui.applicationComponent

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SearchController : MviController<SearchView, SearchPresenter>() {

  override val layoutRes: Int = R.layout.controller_search

  override fun createPresenter(): SearchPresenter = SearchPresenter(applicationComponent().searchEngine(), applicationComponent().analytics())
}
