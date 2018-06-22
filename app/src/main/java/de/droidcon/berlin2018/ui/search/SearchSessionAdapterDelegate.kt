package de.droidcon.berlin2018.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.search.SearchableItem
import de.droidcon.berlin2018.search.SessionSearchableItem

class SearchSessionAdapterDelegate(
    private val layoutInflater: LayoutInflater,
    private val clickListener: (Session) -> Unit
) : AbsListItemAdapterDelegate<SessionSearchableItem, SearchableItem, SearchSessionAdapterDelegate.SessionItemViewHolder>() {


  override fun isForViewType(item: SearchableItem, items: MutableList<SearchableItem>,
      position: Int): Boolean = item is SessionSearchableItem

  override fun onBindViewHolder(item: SessionSearchableItem, viewHolder: SessionItemViewHolder,
      payloads: MutableList<Any>) {
    val session = item.session
    viewHolder.session = session
    viewHolder.title.text = session.title()

    // Time
    if (item.time != null) {
      viewHolder.time.text = item.time
      viewHolder.time.visibility = View.VISIBLE
    } else {
      viewHolder.time.visibility = View.GONE
    }

    // Location
    if (item.location != null) {
      viewHolder.location.visibility = View.VISIBLE
      viewHolder.location.text = item.location
    } else {
      viewHolder.location.visibility = View.GONE
    }

    // Speakers
    val speakers = item.speakers
    if (speakers == null) {
      viewHolder.speakers.visibility = View.GONE
    } else {
      viewHolder.speakers.text = speakers
      viewHolder.speakers.visibility = View.VISIBLE
    }

    viewHolder.favorite.visibility = if (session.favorite()) {
      View.VISIBLE
    } else {
      View.GONE
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup): SessionItemViewHolder =
      SessionItemViewHolder(layoutInflater.inflate(R.layout.item_search_session, parent, false),
          clickListener)

  /**
   * ViewHolder for a Session SessionItem
   */
  class SessionItemViewHolder(v: View, clickListener: (Session) -> Unit) : RecyclerView.ViewHolder(
      v) {

    init {
      v.setOnClickListener({ clickListener(session) })
    }

    val title: TextView = v.findViewById(R.id.title)
    val speakers: TextView = v.findViewById(R.id.speakers)
    val time: TextView = v.findViewById(R.id.time)
    val location: TextView = v.findViewById(R.id.location)
    val favorite: View = v.findViewById(R.id.favorite)
    lateinit var session: Session
  }
}
