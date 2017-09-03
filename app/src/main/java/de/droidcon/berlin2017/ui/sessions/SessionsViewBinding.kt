package de.droidcon.berlin2017.ui.sessions

import android.support.v7.widget.LinearLayoutManager
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.jakewharton.rxbinding2.view.RxView
import com.simplecityapps.recyclerview_fastscroll.interfaces.OnFastScrollStateChangeListener
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import de.droidcon.berlin2017.DroidconApplication
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.model.Session
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
 * Responsible to display a list of all Sessions on screen
 *
 * @author Hannes Dorfmann
 */
open class SessionsViewBinding : ViewBinding(), SessionsView {
  companion object {
    private val KEY_LAST_SCROLL_POSITION = "LastScrollPosition"
  }

  open val loggingTag: String = SessionsViewBinding::class.java.simpleName

  private var recyclerView by LifecycleAwareRef<FastScrollRecyclerView>(this)
  private val scrolledToNowSubject = PublishSubject.create<Boolean>()
  private val retrySubject = PublishSubject.create<Unit>()

  private var loadingView by LifecycleAwareRef<View>(this)
  private var layoutManager by LifecycleAwareRef<LinearLayoutManager>(this)
  private var errorView by LifecycleAwareRef<View>(this)
  private var emptyView by LifecycleAwareRef<View>(this)
  private var rootView by LifecycleAwareRef<ViewGroup>(this)
  private var adapter by LifecycleAwareRef<ListDelegationAdapter<List<SchedulePresentationModel>>>(
      this)

  // It's a one time shot
  private val loadDataIntent = Observable.just(Unit)

  override fun bindView(rootView: ViewGroup) {
    this.rootView = rootView
    val inflater = LayoutInflater.from(rootView.context)

    adapter = SessionsAdapter(
        AdapterDelegatesManager<List<SchedulePresentationModel>>()
            .addDelegate(0, SessionAdapterDelegate(inflater, picasso,
                { onSessionClicked(it) }))
            .addDelegate(1, SessionDayHeaderAdapterDelegate(inflater))
            .addDelegate(2, SessionDayHeaderPlusSearchBoxSpaceAdapterDelegate(inflater))
            .addDelegate(3, SessionTimeSlotDividerAdapterDelegate(inflater))
    )

    layoutManager = LinearLayoutManager(rootView.context)
    val analytics = DroidconApplication.getApplicationComponent(rootView.context).analytics()

    recyclerView = rootView.findViewById(R.id.recyclerView)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = layoutManager
    recyclerView.addOnScrollListener(PicassoScrollListener(picasso))
    recyclerView.setStateChangeListener(object : OnFastScrollStateChangeListener {
      override fun onFastScrollStop() {}

      override fun onFastScrollStart() {
        analytics.trackFastScrollStarted(loggingTag)
      }
    })

    loadingView = rootView.findViewById(R.id.loading)
    errorView = rootView.findViewById(R.id.error)
    emptyView = rootView.findViewById(R.id.empty)

    emptyView.setOnClickListener { onEmptyViewClicked() }
  }

  /**
   * Click listener on empty view
   */
  protected open fun onEmptyViewClicked() {

  }

  protected open fun onSessionClicked(session : Session){
    navigator.showSessionDetails(session)
  }

  override fun loadDataIntent(): Observable<Unit> = loadDataIntent

  override fun scrolledToNowIntent(): Observable<Boolean> = scrolledToNowSubject

  override fun retryLoadDataIntent(): Observable<Unit>  = RxView.clicks(errorView).map { Unit }

  override fun render(state: LceViewState<Sessions>) {
    Timber.tag(loggingTag).d("render $restoringViewState $state")
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
        Timber.tag(loggingTag).d("Scroll To ${state.data.scrollTo}")
        loadingView.gone()
        errorView.gone()
        if (state.data.sessions.isEmpty()) {
          emptyView.visible()
          recyclerView.gone()
        } else {
          if (state.data.diffResult == null || adapter.items == null) {
            adapter.items = state.data.sessions
            adapter.notifyDataSetChanged()
          } else {
            adapter.items = state.data.sessions
            state.data.diffResult.dispatchUpdatesTo(adapter)
          }

          emptyView.gone()
          recyclerView.visible()
          if (state.data.scrollTo != null) {
            recyclerView.scrollToPosition(state.data.scrollTo)
            scrolledToNowSubject.onNext(true)
          }
        }
      }
    }
  }
}