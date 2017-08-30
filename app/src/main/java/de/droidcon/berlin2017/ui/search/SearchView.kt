package de.droidcon.berlin2017.ui.search

import com.hannesdorfmann.mosby3.mvp.MvpView
import de.droidcon.berlin2017.search.SearchableItem
import de.droidcon.berlin2017.ui.lce.LceViewState
import io.reactivex.Observable


typealias SearchResultState = LceViewState<SearchResult>

data class SearchResult (
    val query : String,
    val result : List<SearchableItem>
)

/**
 * Displays the search results
 *
 * @author Hannes Dorfmann
 */
interface SearchView : MvpView {

  fun searchIntent(): Observable<String>

  fun render(state: SearchResultState)
}
