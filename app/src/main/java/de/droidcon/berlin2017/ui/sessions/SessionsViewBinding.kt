package de.droidcon.berlin2017.ui.sessions

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.PicassoScrollListener
import de.droidcon.berlin2017.ui.gone
import de.droidcon.berlin2017.ui.lce.LceViewState
import de.droidcon.berlin2017.ui.viewbinding.LifecycleAwareRef
import de.droidcon.berlin2017.ui.viewbinding.ViewBinding
import de.droidcon.berlin2017.ui.visible
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * Responsible to display a list of all Sessions on screen
 *
 * @author Hannes Dorfmann
 */
class SessionsViewBinding : ViewBinding(), SessionsView {

  private var recyclerView by LifecycleAwareRef<RecyclerView>(this)
  private val scrolledToNowSubject = PublishSubject.create<Boolean>()
  private val retrySubject = PublishSubject.create<Unit>()

  private var loadingView by LifecycleAwareRef<View>(this)
  private var errorView by LifecycleAwareRef<View>(this)
  private var emptyView by LifecycleAwareRef<View>(this)
  private var rootView by LifecycleAwareRef<ViewGroup>(this)
  private var adapter by LifecycleAwareRef<ListDelegationAdapter<List<SchedulePresentationModel>>>(
      this)

  override fun bindView(rootView: ViewGroup) {
    this.rootView = rootView
    val inflater = LayoutInflater.from(rootView.context)

    adapter = SessionsAdapter(
        AdapterDelegatesManager<List<SchedulePresentationModel>>()
            .addDelegate(0, SessionAdapterDelegate(inflater, picasso,
                { navigator.showSessionDetails(it) }))
            .addDelegate(1, SessionDayHeaderAdapterDelegate(inflater))
            .addDelegate(2, SessionDayHeaderPlusSearchBoxSpaceAdapterDelegate(inflater))
    )

    val layoutManager = LinearLayoutManager(rootView.context)

    recyclerView = rootView.findViewById(R.id.recyclerView)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = layoutManager
    recyclerView.addOnScrollListener(PicassoScrollListener(picasso))

    loadingView = rootView.findViewById(R.id.loading)
    errorView = rootView.findViewById(R.id.error)
    emptyView = rootView.findViewById(R.id.empty)


    errorView.setOnClickListener { retrySubject.onNext(Unit) }
  }

  override fun loadDataIntent(): Observable<Unit> = Observable.merge(Observable.just(Unit),
      retrySubject)

  override fun scrolledToNowIntent(): Observable<Boolean> = Observable.merge(Observable.just(false),
      scrolledToNowSubject).delay(200, MILLISECONDS)

  override fun render(state: LceViewState<Sessions>) {
    Timber.d("render $restoringViewState $state")
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
        if (state.data.sessions.isEmpty()) {
          emptyView.visible()
          recyclerView.gone()
        } else {
          adapter.items = state.data.sessions
          adapter.notifyDataSetChanged()
          emptyView.gone()
          recyclerView.visible()
          if (state.data.scrollTo != null) {
            recyclerView.scrollToPosition(state.data.scrollTo)
            scrolledToNowSubject.onNext(true)
            scrolledToNowSubject.onComplete()
          }
        }
      }
    }
  }
}