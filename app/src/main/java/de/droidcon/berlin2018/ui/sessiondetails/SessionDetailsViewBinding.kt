package de.droidcon.berlin2018.ui.sessiondetails

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.bluelinelabs.conductor.Controller
import com.jakewharton.rxbinding2.view.RxView
import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.ui.applicationComponent
import de.droidcon.berlin2018.ui.gone
import de.droidcon.berlin2018.ui.lce.LceViewState
import de.droidcon.berlin2018.ui.lce.LceViewState.Content
import de.droidcon.berlin2018.ui.lce.LceViewState.Loading
import de.droidcon.berlin2018.ui.sessiondetails.SessionDetailsView.SessionState
import de.droidcon.berlin2018.ui.sessions.shortTime
import de.droidcon.berlin2018.ui.sessions.speakerNames
import de.droidcon.berlin2018.ui.viewbinding.LifecycleAwareRef
import de.droidcon.berlin2018.ui.viewbinding.ViewBinding
import de.droidcon.berlin2018.ui.visible
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import org.threeten.bp.ZoneId
import timber.log.Timber

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SessionDetailsViewBinding : ViewBinding(), SessionDetailsView {

  private lateinit var sessionId: String
  private lateinit var session: Session

  private lateinit var zoneConferenceTakesPlace: ZoneId
  private val loadSubject = PublishSubject.create<String>()
  private var fab by LifecycleAwareRef<FloatingActionButton>(this)
  private var rootView by LifecycleAwareRef<ViewGroup>(this)
  private var loadingView by LifecycleAwareRef<View>(this)
  private var errorView by LifecycleAwareRef<View>(this)
  private var contentView by LifecycleAwareRef<View>(this)
  private var speakerPic1 by LifecycleAwareRef<ImageView>(this)
  private var speakerPic2 by LifecycleAwareRef<ImageView>(this)
  private var speakerPic3 by LifecycleAwareRef<ImageView>(this)
  private var speakerPic4 by LifecycleAwareRef<ImageView>(this)
  private var spekersName by LifecycleAwareRef<TextView>(this)
  private var title by LifecycleAwareRef<TextView>(this)
  private var toolbar by LifecycleAwareRef<Toolbar>(this)
  private var time by LifecycleAwareRef<TextView>(this)
  private var location by LifecycleAwareRef<TextView>(this)
  private var description by LifecycleAwareRef<TextView>(this)

  override fun postContextAvailable(controller: Controller, context: Context) {
    sessionId = controller.args.getString(SessionDetailsController.KEY_SESSION_ID)
    zoneConferenceTakesPlace = controller.applicationComponent().clock().getZoneConferenceTakesPlace()
  }

  override fun bindView(rootView: ViewGroup) {
    this.rootView = rootView
    fab = rootView.findViewById(R.id.fab)
    errorView = rootView.findViewById(R.id.error)
    loadingView = rootView.findViewById(R.id.loading)
    contentView = rootView.findViewById(R.id.contentView)
    speakerPic1 = rootView.findViewById(R.id.speakerPic1)
    speakerPic2 = rootView.findViewById(R.id.speakerPic2)
    speakerPic3 = rootView.findViewById(R.id.speakerPic3)
    speakerPic4 = rootView.findViewById(R.id.speakerPic4)
    spekersName = rootView.findViewById(R.id.speakers)
    toolbar = rootView.findViewById(R.id.toolbar)
    title = rootView.findViewById(R.id.title)
    time = rootView.findViewById(R.id.time)
    location = rootView.findViewById(R.id.where)
    description = rootView.findViewById(R.id.description)

    errorView.setOnClickListener { loadSubject.onNext(sessionId) }
    toolbar.setNavigationOnClickListener { navigator.popSelfFromBackstack() }

  }


  override fun loadIntent(): Observable<String> = loadSubject.startWith(sessionId)

  override fun clickOnFabIntent(): Observable<Session> = RxView.clicks(fab).doOnNext {
    Timber.d("Click on fab")
  }.map { session }


  override fun render(state: LceViewState<SessionState>) {
    Timber.d("render $state")

    if (!restoringViewState)
      TransitionManager.beginDelayedTransition(rootView)

    when (state) {
      is Loading -> {
        fab.gone()
        contentView.gone()
        errorView.gone()
        loadingView.visible()
      }
      is Error -> {
        fab.gone()
        contentView.gone()
        errorView.visible()
        loadingView.gone()
      }
      is Content -> {
        session = state.data.session
        if (fab.visibility != View.VISIBLE && !restoringViewState) {
          // TODO use spring animation
          fab.scaleX = 0f
          fab.scaleY = 0f
          fab.visible()
          fab.animate()
              .scaleX(1f)
              .scaleY(1f)
              .setStartDelay(600)
              .setInterpolator(OvershootInterpolator())
              .start()
        } else {
          fab.scaleX = 1f
          fab.scaleY = 1f
          fab.visible()
        }

        if (state.data.favoriteChanged && !restoringViewState) {
          Timber.d("not resoring Session ${session.favorite()}")
          setFabDrawable(!session.favorite())
          fab.startVectorDrawableAnimation()
        } else {
          Timber.d("not changed or restoring Session ${session.favorite()}")
          setFabDrawable(session.favorite())
        }

        errorView.gone()
        loadingView.gone()
        contentView.visible()

        title.text = session.title()
        time.text = session.shortTime(zoneConferenceTakesPlace, true)
        location.text = session.locationName()
        description.text = session.description()

        val speakers = session.speakers()
        spekersName.text = speakers.speakerNames()

        if (speakers.isNotEmpty()) {
          picasso.load(speakers[0].profilePic())
              .transform(CropCircleTransformation())
              .placeholder(R.drawable.speaker_circle_placeholder)
              .into(speakerPic1)

          speakerPic1.setOnClickListener { navigator.showSpeakerDetails(speakers[0]) }
        }

        if (speakers.size >= 2) {
          picasso.load(speakers[1].profilePic())
              .transform(CropCircleTransformation())
              .placeholder(R.drawable.speaker_circle_placeholder)
              .into(speakerPic2)

          speakerPic2.setOnClickListener { navigator.showSpeakerDetails(speakers[1]) }
        }

        if (speakers.size >= 3) {
          picasso.load(speakers[2].profilePic())
              .transform(CropCircleTransformation())
              .placeholder(R.drawable.speaker_circle_placeholder)
              .into(speakerPic3)

          speakerPic3.setOnClickListener { navigator.showSpeakerDetails(speakers[2]) }
        }

        if (speakers.size >= 4) {
          picasso.load(speakers[3].profilePic())
            .transform(CropCircleTransformation())
            .placeholder(R.drawable.speaker_circle_placeholder)
            .into(speakerPic4)

          speakerPic4.setOnClickListener { navigator.showSpeakerDetails(speakers[3]) }
        }

      }
    }
  }


  private fun setFabDrawable(favorite: Boolean) {

    val drawable = fab.drawable as AnimatedVectorDrawable
    drawable.stop()

    if (favorite) {
      fab.setImageDrawable(
          fab.context.resources.getDrawable(
              R.drawable.avd_remove_from_schedule,
              fab.context.theme)!!.mutate().constantState.newDrawable())
    } else {
      fab.setImageDrawable(
          fab.context.resources.getDrawable(
              R.drawable.avd_add_to_schedule,
              fab.context.theme)!!.mutate().constantState.newDrawable())
    }
  }


}

fun FloatingActionButton.startVectorDrawableAnimation() {
  val drawable = drawable as AnimatedVectorDrawable
  drawable.start()
}
