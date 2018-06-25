package de.droidcon.berlin2018.ui.speakers

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.squareup.picasso.Picasso
import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.model.Speaker

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SpeakersAdapterDelegate(
    private val inflater: LayoutInflater,
    private val picasso: Picasso,
    private val clicklistener: (Speaker) -> Unit
) : AbsListItemAdapterDelegate<Speaker, Any, SpeakersAdapterDelegate.SpeakerViewHolder>() {


  override fun onCreateViewHolder(parent: ViewGroup): SpeakerViewHolder {
    val view = inflater.inflate(R.layout.item_speaker, parent, false)
    return SpeakerViewHolder(view)
  }

  override fun onBindViewHolder(item: Speaker, viewHolder: SpeakerViewHolder,
      payloads: MutableList<Any>) {
    viewHolder.bind(item)
  }


  override fun isForViewType(item: Any, items: MutableList<Any>,
      position: Int): Boolean = item is Speaker


  inner class SpeakerViewHolder(v: View) : ViewHolder(v) {
    private lateinit var speaker: Speaker
    private val name = v.findViewById<TextView>(R.id.name)
    private val image = v.findViewById<ImageView>(R.id.image)

    init {
      v.setOnClickListener { clicklistener(speaker) }
    }

    fun bind(speaker: Speaker) {
      this.speaker = speaker
      name.text = speaker.name()
      picasso.load(speaker.profilePic())
          .fit()
          .centerCrop()
          .placeholder(R.color.speakerslist_placeholder)
          .into(image)
    }

  }

}
