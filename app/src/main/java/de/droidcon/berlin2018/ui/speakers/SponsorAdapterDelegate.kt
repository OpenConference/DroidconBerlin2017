package de.droidcon.berlin2018.ui.speakers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.squareup.picasso.Picasso
import de.droidcon.berlin2018.R


/**
 *
 *
 * @author Hannes Dorfmann
 */
class SponsorAdapterDelegate(
    private val inflater: LayoutInflater,
    private val picasso: Picasso,
    private val activity: Activity
) : AbsListItemAdapterDelegate<SponsorItem, Any, SponsorAdapterDelegate.SponsorViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup): SponsorViewHolder {
        val view = inflater.inflate(R.layout.item_sponsor, parent, false)
        return SponsorViewHolder(activity, view)
    }

    override fun onBindViewHolder(
        item: SponsorItem, viewHolder: SponsorViewHolder,
        payloads: MutableList<Any>
    ) {
        viewHolder.bind(item)
    }


    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
        item is SponsorItem

    inner class SponsorViewHolder(activity: Activity, v: View) : ViewHolder(v) {
        private lateinit var sponsor: SponsorItem
        private val image = v.findViewById<ImageView>(R.id.image)

        init {
            image.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(sponsor.link)
                activity.startActivity(i)
            }
        }

        fun bind(sponsor: SponsorItem) {
            this.sponsor = sponsor
            picasso.load(sponsor.drawable)
                .fit()
                .centerInside()
                .placeholder(R.color.speakerslist_placeholder)
                .into(image)
        }

    }

}
