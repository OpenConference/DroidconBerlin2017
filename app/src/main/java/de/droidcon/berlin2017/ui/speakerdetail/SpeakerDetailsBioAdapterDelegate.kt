package de.droidcon.berlin2017.ui.speakerdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.openconference.speakerdetails.SpeakerDetailsViewHolder
import de.droidcon.berlin2017.R

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SpeakerDetailsBioAdapterDelegate(
    val inflater: LayoutInflater) : AbsListItemAdapterDelegate<SpeakerBioItem, SpeakerDetailsItem, SpeakerDetailsViewHolder>() {


  override fun isForViewType(item: SpeakerDetailsItem, items: MutableList<SpeakerDetailsItem>,
      position: Int): Boolean = item is SpeakerBioItem

  override fun onBindViewHolder(item: SpeakerBioItem, viewHolder: SpeakerDetailsViewHolder,
      payloads: MutableList<Any>) {
    viewHolder.bind(null, item.bio)
  }

  override fun onCreateViewHolder(parent: ViewGroup): SpeakerDetailsViewHolder {
    val view = inflater.inflate(R.layout.item_details_icon_text, parent, false)
    view.isClickable = false
    return SpeakerDetailsViewHolder(view)
  }

}