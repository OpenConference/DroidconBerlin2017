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
class SpeakerDetailsLinkAdapterDelegate(
    val inflater: LayoutInflater) : AbsListItemAdapterDelegate<SpeakerLinkItem, SpeakerDetailsItem, SpeakerDetailsViewHolder>() {

  override fun isForViewType(item: SpeakerDetailsItem, items: MutableList<SpeakerDetailsItem>,
      position: Int): Boolean =item is SpeakerLinkItem

  override fun onBindViewHolder(item: SpeakerLinkItem, viewHolder: SpeakerDetailsViewHolder,
      payloads: MutableList<Any>) {

    viewHolder.bind(if (item.showIcon) {
      R.drawable.ic_link
    } else null, item.url)

  }

  override fun onCreateViewHolder(parent: ViewGroup) = SpeakerDetailsViewHolder(
      inflater.inflate(R.layout.item_details_icon_link_text, parent, false))

}