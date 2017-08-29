package de.droidcon.berlin2017.ui.sessions

import android.support.v7.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView.MeasurableAdapter
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView.SectionedAdapter
import de.droidcon.berlin2017.R

/**
 * Responsible to show a list of all sessions
 *
 * @author Hannes Dorfmann
 */
class SessionsAdapter(
    delegatesManager: AdapterDelegatesManager<List<SchedulePresentationModel>>) : ListDelegationAdapter<List<SchedulePresentationModel>>(
    delegatesManager), MeasurableAdapter, SectionedAdapter {

  override fun getViewTypeHeight(recyclerView: RecyclerView, viewType: Int): Int {

    return when (viewType) {
      0 -> recyclerView.resources.getDimensionPixelOffset(R.dimen.session_item_height)
      1 -> recyclerView.resources.getDimensionPixelOffset(R.dimen.subheader_height)
      else -> throw IllegalArgumentException(
          "Don't know how to compute the height of item with viewType = $viewType")
    }
  }


  override fun getSectionName(position: Int): String = items[position].fastScrollInfo
}