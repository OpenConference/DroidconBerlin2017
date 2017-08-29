package de.droidcon.berlin2017.ui.sessions

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.sessions.SchedulePresentationModel.DayPresentationModel
import de.droidcon.berlin2017.ui.sessions.SessionDayHeaderAdapterDelegate.DayViewHolder

/**
 * Displays a date separator
 *
 * @author Hannes Dorfmann
 */
class SessionDayHeaderAdapterDelegate(
    private val inflater: LayoutInflater
) : AbsListItemAdapterDelegate<SchedulePresentationModel.DayPresentationModel, SchedulePresentationModel, DayViewHolder>() {


  override fun isForViewType(item: SchedulePresentationModel,
      items: MutableList<SchedulePresentationModel>,
      position: Int): Boolean = item is DayPresentationModel

  override fun onCreateViewHolder(parent: ViewGroup): DayViewHolder = DayViewHolder(
      inflater.inflate(R.layout.item_session_day_header, parent, false))

  override fun onBindViewHolder(item: DayPresentationModel, viewHolder: DayViewHolder,
      payloads: MutableList<Any>) {
    viewHolder.bind(item)
  }

  inner class DayViewHolder(v: View) : ViewHolder(v) {
    private val dayInWeek = v.findViewById<TextView>(R.id.dayInWeek)
    private val date = v.findViewById<TextView>(R.id.date)

    fun bind(day: DayPresentationModel) {
      dayInWeek.text = day.dayString
      date.text = day.dateString
    }
  }
}