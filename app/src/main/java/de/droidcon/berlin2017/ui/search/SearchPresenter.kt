package de.droidcon.berlin2017.ui.search

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2017.analytics.Analytics
import de.droidcon.berlin2017.search.SearchEngine
import de.droidcon.berlin2017.ui.lce.LceViewState
import de.droidcon.berlin2017.ui.lce.lceObservable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * Presenter who is responsible to deal with Search queries
 *
 * @author Hannes Dorfmann
 */
class SearchPresenter(
    private val searchEngine: SearchEngine,
    private val analytics: Analytics) : MviBasePresenter<SearchView, SearchResultState>() {


  override fun bindIntents() {
    val search = intent(SearchView::searchIntent)
        .filter { it.isEmpty() || it.length > 2 }
        .debounce(200, MILLISECONDS)
        .doOnNext { Timber.d("Search intent: $it") }
        .doOnNext { if (it.isNotEmpty()) analytics.trackSearch(it) }

    val result: Observable<SearchResultState> = search.switchMap { query ->
      if (query.isEmpty())
        Observable.just(LceViewState.Content(SearchResult(query, emptyList())))
      else
        lceObservable(searchEngine.search(query).map { SearchResult(query, it) }).subscribeOn(
            Schedulers.io())
    }
        .observeOn(AndroidSchedulers.mainThread())

    subscribeViewState(result, SearchView::render)

  }
}