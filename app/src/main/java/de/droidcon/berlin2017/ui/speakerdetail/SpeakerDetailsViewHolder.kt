package com.openconference.speakerdetails

import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import de.droidcon.berlin2017.R

/**
 * ViewHolder for icon text combination
 *
 * @author Hannes Dorfmann
 */
open class SpeakerDetailsViewHolder(v: View) : RecyclerView.ViewHolder(v) {

  val icon = v.findViewById<ImageView>(R.id.icon)
  val text = v.findViewById<TextView>(R.id.text)

  inline fun bind(@DrawableRes iconRes: Int?, t: String) {
    if (iconRes == null) {
      icon.visibility = View.INVISIBLE
    } else {
      icon.setImageDrawable(itemView.resources.getDrawable(iconRes))
    }

    text.text = t
  }
}
