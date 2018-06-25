package de.droidcon.berlin2018.ui.speakers

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import de.droidcon.berlin2018.R


/**
 *
 *
 * @author Hannes Dorfmann
 */
class SponsorSectionTitleAdapterDelegate(
    private val inflater: LayoutInflater
) : AbsListItemAdapterDelegate<SponsorSectionTitle, Any, SponsorSectionTitleAdapterDelegate.SponsorViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup): SponsorViewHolder {
        val view = inflater.inflate(R.layout.item_sponsors_title, parent, false)
        return SponsorViewHolder( view)
    }

    override fun onBindViewHolder(
        item: SponsorSectionTitle, viewHolder: SponsorViewHolder,
        payloads: MutableList<Any>
    ) {}


    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
        item == SponsorSectionTitle

    inner class SponsorViewHolder(v: View) : ViewHolder(v)

}
