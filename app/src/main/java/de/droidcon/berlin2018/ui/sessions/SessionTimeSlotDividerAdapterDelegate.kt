package de.droidcon.berlin2018.ui.sessions

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.R.layout
import de.droidcon.berlin2018.ui.sessions.SchedulePresentationModel.TimeSlotDividerPresentationModel
import de.droidcon.berlin2018.ui.sessions.SessionTimeSlotDividerAdapterDelegate.TimeslotViewHolder

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SessionTimeSlotDividerAdapterDelegate(
    private val inflater: LayoutInflater) : AbsListItemAdapterDelegate<TimeSlotDividerPresentationModel, SchedulePresentationModel, TimeslotViewHolder>() {
  override fun isForViewType(item: SchedulePresentationModel,
      items: MutableList<SchedulePresentationModel>,
      position: Int): Boolean = item is TimeSlotDividerPresentationModel

  override fun onCreateViewHolder(parent: ViewGroup): TimeslotViewHolder = TimeslotViewHolder(
      inflater.inflate(layout.item_sessions_timeslot_separator, parent, false))

  override fun onBindViewHolder(item: TimeSlotDividerPresentationModel, viewHolder: TimeslotViewHolder,
      payloads: MutableList<Any>) {
    viewHolder.time.text = item.timeStr
  }

  inner class TimeslotViewHolder(v : View) : ViewHolder(v){
    val time = v.findViewById<TextView>(R.id.timeSlot)
  }
}
