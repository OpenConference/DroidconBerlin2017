package de.droidcon.berlin2018.ui.speakerdetail

import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.model.Session

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SpeakerDetailsSessionDelegate(val inflater: LayoutInflater,
    val clickListener: (Session) -> Unit) : AbsListItemAdapterDelegate<SpeakerSessionItem, SpeakerDetailsItem, SpeakerDetailsSessionDelegate.SessionViewHolder>() {


  override fun isForViewType(item: SpeakerDetailsItem, items: MutableList<SpeakerDetailsItem>,
      position: Int): Boolean = item is SpeakerSessionItem

  override fun onBindViewHolder(item: SpeakerSessionItem, viewHolder: SessionViewHolder,
      payloads: MutableList<Any>) {
    viewHolder.session = item.session
    viewHolder.bind(if (item.showIcon) R.drawable.ic_sessions_details else null,
        item.session.title()!!)
  }

  override fun onCreateViewHolder(parent: ViewGroup): SessionViewHolder =
      SessionViewHolder(inflater.inflate(R.layout.item_details_icon_text, parent, false),
          clickListener)

  class SessionViewHolder(v: View, val clickListener: (Session) -> Unit) : RecyclerView.ViewHolder(
      v) {

    lateinit var session: Session

    init {
      v.setOnClickListener { clickListener(session) }
    }

    val icon = v.findViewById<ImageView>(R.id.icon)
    val text = v.findViewById<TextView>(R.id.text)

    inline fun bind(@DrawableRes iconRes: Int?, t: String) {
      if (iconRes == null) {
        icon.visibility = View.INVISIBLE
      } else {
        icon.setImageDrawable(itemView.resources.getDrawable(iconRes))
      }

      text.text = t
    }
  }
}
