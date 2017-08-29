package de.droidcon.berlin2017.ui.sessions

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import de.droidcon.berlin2017.R.id
import de.droidcon.berlin2017.R.layout
import de.droidcon.berlin2017.ui.sessions.SchedulePresentationModel.DayPresentationModel
import de.droidcon.berlin2017.ui.sessions.SessionDayHeaderPlusSearchBoxSpaceAdapterDelegate.DayViewHolder

/**
 * Displays a date separator
 *
 * @author Hannes Dorfmann
 */
class SessionDayHeaderPlusSearchBoxSpaceAdapterDelegate(
    private val inflater: LayoutInflater
) : AbsListItemAdapterDelegate<DayPresentationModel, SchedulePresentationModel, DayViewHolder>() {


  override fun isForViewType(item: SchedulePresentationModel,
      items: MutableList<SchedulePresentationModel>,
      position: Int): Boolean = position == 0 && item is DayPresentationModel

  override fun onCreateViewHolder(parent: ViewGroup): DayViewHolder = DayViewHolder(
      inflater.inflate(layout.item_session_day_header_plus_searchbox_space, parent, false))

  override fun onBindViewHolder(item: DayPresentationModel, viewHolder: DayViewHolder,
      payloads: MutableList<Any>) {
    viewHolder.bind(item)
  }

  inner class DayViewHolder(v: View) : ViewHolder(v) {
    private val dayInWeek = v.findViewById<TextView>(id.dayInWeek)
    private val date = v.findViewById<TextView>(id.date)

    fun bind(day: DayPresentationModel) {
      dayInWeek.text = day.dayString
      date.text = day.dateString
    }
  }
}