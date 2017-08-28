package de.droidcon.berlin2017.ui.sessions

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.sessions.SchedulePresentationModel.TimeSlotDividerPresentationModel

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SessionTimeSlotDividerAdapterDelegate(
    private val inflater: LayoutInflater) : AbsListItemAdapterDelegate<TimeSlotDividerPresentationModel, SchedulePresentationModel, ViewHolder>() {
  override fun isForViewType(item: SchedulePresentationModel,
      items: MutableList<SchedulePresentationModel>,
      position: Int): Boolean = item is TimeSlotDividerPresentationModel

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = object : ViewHolder(
      inflater.inflate(R.layout.item_sessions_timeslot_separator, parent, false)) {}

  override fun onBindViewHolder(item: TimeSlotDividerPresentationModel, viewHolder: ViewHolder,
      payloads: MutableList<Any>) {
  }
}