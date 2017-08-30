package de.droidcon.berlin2017.ui.speakers

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.model.Speaker
import de.droidcon.berlin2017.ui.PicassoScrollListener
import de.droidcon.berlin2017.ui.gone
import de.droidcon.berlin2017.ui.lce.LceViewState
import de.droidcon.berlin2017.ui.viewbinding.LifecycleAwareRef
import de.droidcon.berlin2017.ui.viewbinding.ViewBinding
import de.droidcon.berlin2017.ui.visible
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

/**
 * UI that displays the list of speakers
 *
 * @author Hannes Dorfmann
 */
class SpeakersViewBinding : ViewBinding(), SpeakersView {

  private var loadingView by LifecycleAwareRef<View>(this)
  private var errorView by LifecycleAwareRef<View>(this)
  private var emptyView by LifecycleAwareRef<View>(this)
  private var recyclerView by LifecycleAwareRef<RecyclerView>(this)
  private var rootView by LifecycleAwareRef<ViewGroup>(this)
  private var adapter by LifecycleAwareRef<ListDelegationAdapter<List<Speaker>>>(this)

  private val retrySubject = PublishSubject.create<Unit>()

  override fun bindView(rootView: ViewGroup) {
    this.rootView = rootView
    adapter = ListDelegationAdapter(
        AdapterDelegatesManager<List<Speaker>>()
            .addDelegate(SpeakersAdapterDelegate(LayoutInflater.from(rootView.context), picasso,
                { navigator.showSpeakerDetails(it) }))
    )

    val layoutManager = GridLayoutManager(rootView.context,
        rootView.resources.getInteger(R.integer.speakers_list_columns))
    layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int = 1
    }

    recyclerView = rootView.findViewById(R.id.recyclerView)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = layoutManager
    recyclerView.addOnScrollListener(PicassoScrollListener(picasso))

    loadingView = rootView.findViewById(R.id.loading)
    errorView = rootView.findViewById(R.id.error)
    emptyView = rootView.findViewById(R.id.empty)


    errorView.setOnClickListener { retrySubject.onNext(Unit) }

  }

  override fun loadIntent(): Observable<Unit> = retrySubject.startWith(Unit) // trigger on subscribe

  override fun render(state: LceViewState<List<Speaker>>) {
    Timber.d("render $state")
    if (!restoringViewState)
      TransitionManager.beginDelayedTransition(rootView)
    when (state) {
      is LceViewState.Loading -> {
        errorView.gone()
        emptyView.gone()
        recyclerView.gone()
        loadingView.visible()
      }
      is LceViewState.Error -> {
        loadingView.gone()
        emptyView.gone()
        recyclerView.gone()
        errorView.visible()
      }
      is LceViewState.Content -> {
        loadingView.gone()
        errorView.gone()
        if (state.data.isEmpty()) {
          emptyView.visible()
          recyclerView.gone()
        } else {
          adapter.items = state.data
          adapter.notifyDataSetChanged()
          emptyView.gone()
          recyclerView.visible()
        }
      }
    }
  }
}