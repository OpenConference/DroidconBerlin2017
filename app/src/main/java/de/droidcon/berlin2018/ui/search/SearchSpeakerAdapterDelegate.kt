package de.droidcon.berlin2018.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.squareup.picasso.Picasso
import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.model.Speaker
import de.droidcon.berlin2018.search.SearchableItem
import de.droidcon.berlin2018.search.SpeakerSearchableItem
import de.droidcon.berlin2018.ui.PicassoScrollListener
import jp.wasabeef.picasso.transformations.CropCircleTransformation

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SearchSpeakerAdapterDelegate(
    private val inflater: LayoutInflater,
    private val picasso: Picasso,
    private val clickListener: (Speaker) -> Unit
) : AbsListItemAdapterDelegate<SpeakerSearchableItem, SearchableItem, SearchSpeakerAdapterDelegate.SpeakerViewHolder>() {


  override fun isForViewType(item: SearchableItem, items: MutableList<SearchableItem>,
      position: Int): Boolean = item is SpeakerSearchableItem

  override fun onCreateViewHolder(parent: ViewGroup): SpeakerViewHolder =
      SpeakerViewHolder(
          inflater.inflate(R.layout.item_search_speaker, parent, false), picasso,
          clickListener)

  override fun onBindViewHolder(item: SpeakerSearchableItem, viewHolder: SpeakerViewHolder,
      payloads: MutableList<Any>) {
    viewHolder.bind(item.speaker)
  }

  class SpeakerViewHolder(v: View, val picasso: Picasso, val clickListener: (Speaker) -> Unit) : RecyclerView.ViewHolder(
      v) {

    init {
      v.setOnClickListener { clickListener(speaker) }
    }

    val name: TextView = v.findViewById(R.id.name)
    val company: TextView = v.findViewById(R.id.company)
    val profilePic: ImageView =v.findViewById(R.id.profilePic)
    lateinit var speaker: Speaker

    inline fun bind(s: Speaker) {
      speaker = s
      name.text = s.name()
      val companyName = s.company()
      val jobTitle = s.jobTitle()

      picasso.load(s.profilePic())
          .placeholder(R.drawable.speaker_circle_placeholder)
          .tag(PicassoScrollListener.TAG)
          .transform(CropCircleTransformation())
          .into(profilePic)

      val companyJobTitle = StringBuilder()
      if (companyName != null) {
        companyJobTitle.append(companyName)
      }

      if (companyName != null && jobTitle != null) {
        companyJobTitle.append(", ")
      }

      if (jobTitle != null) {
        companyJobTitle.append(jobTitle)
      }

      if (companyJobTitle.length > 0) {
        company.text = companyJobTitle.toString()
        company.visibility = View.VISIBLE
      } else {
        company.visibility = View.GONE
      }
    }
  }
}
