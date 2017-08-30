package de.droidcon.berlin2017.ui.speakerdetail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import de.droidcon.berlin2017.R

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SpeakerDetailsJobInfoAdapterDelegate(
    val inflater: LayoutInflater) : AbsListItemAdapterDelegate<SpeakerJobInfoItem, SpeakerDetailsItem, SpeakerDetailsJobInfoAdapterDelegate.JobInfoViewHolder>() {

  override fun isForViewType(item: SpeakerDetailsItem, items: MutableList<SpeakerDetailsItem>,
      position: Int): Boolean = item is SpeakerJobInfoItem

  override fun onBindViewHolder(item: SpeakerJobInfoItem, viewHolder: JobInfoViewHolder,
      payloads: MutableList<Any>) {
    viewHolder.bind(item)
  }

  override fun onCreateViewHolder(parent: ViewGroup) = JobInfoViewHolder(
      inflater.inflate(R.layout.item_speaker_details_jobinfo, parent, false))

  class JobInfoViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val jobTitle = v.findViewById<TextView>(R.id.jobTitle)
    val companyName = v.findViewById<TextView>(R.id.company)

    inline fun bind(info: SpeakerJobInfoItem) {
      val title = info.jobTitle
      if (title != null) {
        jobTitle.text = title
        jobTitle.visibility = View.VISIBLE
      } else {
        jobTitle.visibility = View.GONE
      }

      val company = info.company
      if (company != null) {
        companyName.text = company
        companyName.visibility = View.VISIBLE
      } else {
        companyName.visibility = View.GONE
      }
    }
  }

}