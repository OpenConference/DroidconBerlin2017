package de.droidcon.berlin2017.ui.sessions

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.squareup.picasso.Picasso
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.ui.PicassoScrollListener
import de.droidcon.berlin2017.ui.gone
import de.droidcon.berlin2017.ui.sessions.SchedulePresentationModel.SessionPresentationModel
import de.droidcon.berlin2017.ui.sessions.SessionAdapterDelegate.SessionViewHolder
import de.droidcon.berlin2017.ui.visible
import jp.wasabeef.picasso.transformations.CropCircleTransformation

/**
 * Displays a session
 *
 * @author Hannes Dorfmann
 */
class SessionAdapterDelegate(
    private val inflater: LayoutInflater,
    private val picasso: Picasso,
    private val clickListener: (Session) -> Unit
) : AbsListItemAdapterDelegate<SessionPresentationModel, SchedulePresentationModel, SessionViewHolder>() {

  override fun isForViewType(item: SchedulePresentationModel,
      items: MutableList<SchedulePresentationModel>,
      position: Int): Boolean = item is SessionPresentationModel

  override fun onBindViewHolder(item: SessionPresentationModel, viewHolder: SessionViewHolder,
      payloads: MutableList<Any>) {
    viewHolder.bind(item)
  }

  override fun onCreateViewHolder(parent: ViewGroup): SessionViewHolder = SessionViewHolder(
      inflater.inflate(R.layout.item_session, parent, false)
  )

  inner class SessionViewHolder(v: View) : ViewHolder(v) {

    init {
      v.setOnClickListener { clickListener(session) }
    }

    private lateinit var session: Session
    private val authorPic1 = v.findViewById<ImageView>(R.id.authorPic1)
    private val authorPic2 = v.findViewById<ImageView>(R.id.authorPic2)
    private val authorPic3 = v.findViewById<ImageView>(R.id.authorPic3)
    private val speakerNames = v.findViewById<TextView>(R.id.speakers)
    private val time = v.findViewById<TextView>(R.id.time)
    private val favorite = v.findViewById<View>(R.id.favorite)

    fun bind(session: SessionPresentationModel) {

      authorPic1.gone()
      authorPic2.gone()
      authorPic3.gone()

      if (session.speakers.isNotEmpty()) {
        val speaker = session.speakers[0]
        picasso.load(speaker.profilePic())
            .placeholder(R.color.speakerslist_placeholder)
            .transform(CropCircleTransformation())
            .tag(PicassoScrollListener.TAG)
            .into(authorPic1)
        authorPic1.visible()
      }

      if (session.speakers.size >= 2) {
        val speaker = session.speakers[1]
        picasso.load(speaker.profilePic())
            .placeholder(R.color.speakerslist_placeholder)
            .transform(CropCircleTransformation())
            .tag(PicassoScrollListener.TAG)
            .into(authorPic2)
        authorPic2.visible()
      }


      if (session.speakers.size >= 3) {
        val speaker = session.speakers[2]
        picasso.load(speaker.profilePic())
            .placeholder(R.color.speakerslist_placeholder)
            .transform(CropCircleTransformation())
            .tag(PicassoScrollListener.TAG)
            .into(authorPic3)
        authorPic3.visible()
      }


      speakerNames.text = session.speakerNames

      if (session.time == null) {
        time.gone()
      } else {
        time.text = session.time
        time.visible()
      }

      if (session.favorite)
        favorite.visibility
      else
        favorite.gone()
    }
  }
}