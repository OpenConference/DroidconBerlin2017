package de.droidcon.berlin2017.ui.search

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.search.SearchableItem
import de.droidcon.berlin2017.ui.gone
import de.droidcon.berlin2017.ui.lce.LceViewState
import de.droidcon.berlin2017.ui.searchbox.SearchBox
import de.droidcon.berlin2017.ui.viewbinding.LifecycleAwareRef
import de.droidcon.berlin2017.ui.viewbinding.ViewBinding
import de.droidcon.berlin2017.ui.visible
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SearchViewBinding : ViewBinding(), SearchView {

  var searchBox by LifecycleAwareRef<SearchBox>(this)
  var root by LifecycleAwareRef<View>(this)
  var loading by LifecycleAwareRef<View>(this)
  var recyclerView by LifecycleAwareRef<RecyclerView>(this)
  var resultsWrapper by LifecycleAwareRef<ViewGroup>(this)
  var emptyView by LifecycleAwareRef<View>(this)
  var errorView by LifecycleAwareRef<View>(this)
  var container by LifecycleAwareRef<ViewGroup>(this)
  private var adapter by LifecycleAwareRef<ListDelegationAdapter<List<SearchableItem>>>(this)
  private var autoTransition by LifecycleAwareRef<Transition>(this)
  private val retrySubject = PublishSubject.create<String>()

  override fun bindView(rootView: ViewGroup) {
    root = rootView.findViewById(R.id.searchRoot)
    searchBox = rootView.findViewById(R.id.searchBox)
    recyclerView = rootView.findViewById(R.id.contentView)
    resultsWrapper = rootView.findViewById(R.id.resultsWrapper)
    loading = rootView.findViewById(R.id.loadingView)
    emptyView = rootView.findViewById(R.id.noResult)
    container = rootView.findViewById(R.id.container)
    errorView = rootView.findViewById(R.id.error)
    errorView.setOnClickListener { retrySubject.onNext(searchBox.currentSearchText()) }

    searchBox.showInput = true
    searchBox.requestFocusForSearchInput()
    autoTransition = TransitionInflater.from(rootView.context).inflateTransition(R.transition.auto)

    val inflater = LayoutInflater.from(rootView.context)

    adapter = ListDelegationAdapter(AdapterDelegatesManager<List<SearchableItem>>()
        .addDelegate(SearchSessionAdapterDelegate(inflater, { navigator.showSessionDetails(it) }))
        .addDelegate(
            SearchSpeakerAdapterDelegate(inflater, picasso, { navigator.showSpeakerDetails(it) }))
    )
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)

    root.setOnClickListener { navigator.showHome() }
  }

  override fun searchIntent(): Observable<String> = Observable.merge(
      searchBox.textInputChanges,
      retrySubject
  )

  override fun render(state: SearchResultState) {
    Timber.d("Render $state")
    if (!restoringViewState)
      TransitionManager.beginDelayedTransition(container, autoTransition)
    when (state) {
      is LceViewState.Loading -> {
        loading.visible()
        errorView.gone()
        recyclerView.gone()
        emptyView.gone()

      }
      is LceViewState.Error -> {
        loading.gone()
        errorView.visible()
        recyclerView.gone()
        emptyView.gone()

      }
      is LceViewState.Content -> {
        loading.gone()
        errorView.gone()
        when {
          state.data.query.isEmpty() -> {
            recyclerView.gone()
            emptyView.gone()
          }
          state.data.result.isEmpty() -> {
            recyclerView.gone()
            emptyView.visible()
          }
          else -> {
            adapter.items = state.data.result
            adapter.notifyDataSetChanged()
            emptyView.gone()
            recyclerView.visible()
          }
        }
      }
    }
  }


}