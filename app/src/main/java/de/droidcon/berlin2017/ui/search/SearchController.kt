package de.droidcon.berlin2017.ui.search

import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.MviController
import de.droidcon.berlin2017.ui.applicationComponent

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SearchController : MviController<SearchView, SearchPresenter>() {

  override val layoutRes: Int = R.layout.controller_search

  override fun createPresenter(): SearchPresenter = SearchPresenter(applicationComponent().searchEngine(), applicationComponent().analytics())
}