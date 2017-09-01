package de.droidcon.berlin2017.ui.speakerdetail

import android.content.Context
import android.graphics.PorterDuff
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bluelinelabs.conductor.Controller
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.gone
import de.droidcon.berlin2017.ui.lce.LceViewState
import de.droidcon.berlin2017.ui.lce.LceViewState.Content
import de.droidcon.berlin2017.ui.lce.LceViewState.Error
import de.droidcon.berlin2017.ui.lce.LceViewState.Loading
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
class SpeakerDetailsViewBinding : ViewBinding(), SpeakerDetailsView {

  private lateinit var speakerId: String

  private var loadingView by LifecycleAwareRef<View>(this)
  private var errorView by LifecycleAwareRef<View>(this)
  private var emptyView by LifecycleAwareRef<View>(this)
  private var recyclerView by LifecycleAwareRef<RecyclerView>(this)
  private var rootView by LifecycleAwareRef<ViewGroup>(this)
  private var contentView by LifecycleAwareRef<ViewGroup>(this)
  private var adapter by LifecycleAwareRef<ListDelegationAdapter<List<SpeakerDetailsItem>>>(this)
  private var collapsingToolbar: CollapsingToolbarLayout? = null
  private var toolbar by LifecycleAwareRef<Toolbar>(this)
  private var profilePic: ImageView? = null
  private val loadSubject = PublishSubject.create<String>()

  override fun postContextAvailable(controller: Controller, context: Context) {
    // TODO refactor this
    speakerId = controller.args.getString(SpeakerDetailsController.KEY_SPEAKER_ID)!!
  }


  override fun bindView(rootView: ViewGroup) {
    this.rootView = rootView
    contentView = rootView.findViewById(R.id.contentView)
    collapsingToolbar = rootView.findViewById(R.id.collapsingToolbar)
    toolbar = rootView.findViewById(R.id.toolbar)
    profilePic = rootView.findViewById(R.id.image)
    recyclerView = rootView.findViewById(R.id.recyclerView)
    emptyView = rootView.findViewById(R.id.emptyView)
    errorView = rootView.findViewById(R.id.error)
    loadingView = rootView.findViewById(R.id.loading)

    profilePic?.setColorFilter(
        rootView.resources.getColor(R.color.profilepic_darken),
        PorterDuff.Mode.DARKEN)

    errorView.setOnClickListener { loadSubject.onNext(speakerId) }
    toolbar.setNavigationOnClickListener { navigator.popSelfFromBackstack() }

    val inflater = LayoutInflater.from(rootView.context)
    adapter = ListDelegationAdapter(
        AdapterDelegatesManager<List<SpeakerDetailsItem>>()
            .addDelegate(SpeakerDetailsBioAdapterDelegate(inflater))
            .addDelegate(SpeakerDetailsJobInfoAdapterDelegate(inflater))
            .addDelegate(SpeakerDetailsLinkAdapterDelegate(inflater))
            .addDelegate(
                SpeakerDetailsSessionDelegate(inflater, { navigator.showSessionDetails(it) }))
    )

    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(rootView.context)
  }

  override fun loadIntent(): Observable<String> = loadSubject.startWith(speakerId)

  override fun render(state: LceViewState<SpeakerDetail>) {
    Timber.d("Render $state")
    if (!restoringViewState)
      TransitionManager.beginDelayedTransition(rootView)
    when (state) {
      is Loading -> {
        contentView.gone()
        errorView.gone()
        loadingView.visible()
      }
      is Error -> {
        contentView.gone()
        errorView.visible()
        loadingView.gone()
      }
      is Content -> {
        val name = state.data.speakerName
        toolbar.title = name
        collapsingToolbar?.title = name
        errorView.gone()
        loadingView.gone()
        contentView.visible()

        if (profilePic != null)
          picasso.load(state.data.profilePic)
              .centerCrop()
              .fit()
              .placeholder(R.color.speakerslist_placeholder)
              .into(profilePic)

        val info = state.data.detailsItems
        if (info.isEmpty()) {
          recyclerView.gone()
          emptyView.visible()
        } else {
          adapter.items = info
          adapter.notifyDataSetChanged()
          recyclerView.visible()
          emptyView.gone()
        }
      }
    }

  }

  override fun postDestroyView(controller: Controller) {
    super.postDestroyView(controller)
    collapsingToolbar = null
    profilePic = null
  }

}