package de.droidcon.berlin2017.ui.searchbox

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.dpToPx

/**
 *
 *
 * @author Hannes Dorfmann
 */
class SearchBox(context: Context, attributeSet: AttributeSet) : CardView(context,
    attributeSet) {

  val searchIcon: ImageView by lazy { findViewById<ImageView>(R.id.searchIcon) }
  val searchField: EditText by lazy { findViewById<EditText>(R.id.searchInput) }
  val clearSearch: View by lazy { findViewById<View>(R.id.clearSearch) }

  init {
    val inflater = LayoutInflater.from(context)
    inflater.inflate(R.layout.view_searchbox, this, true)
    cardElevation = dpToPx(4.0f)
    useCompatPadding = true

    /*
    val padding= dpToPx(R.dimen.activity_horizontal_margin).toInt()
    setPadding(padding, padding, padding, padding)
    clipToPadding = false
    */
  }

}