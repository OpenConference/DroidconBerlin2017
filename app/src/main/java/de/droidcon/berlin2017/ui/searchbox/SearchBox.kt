package de.droidcon.berlin2017.ui.searchbox

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.jakewharton.rxbinding2.widget.RxTextView
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.gone
import de.droidcon.berlin2017.ui.hideKeyboard
import de.droidcon.berlin2017.ui.showKeyboard
import de.droidcon.berlin2017.ui.visible
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_searchbox.view.searchInput

/**
 * Custom View for searching
 *
 * @author Hannes Dorfmann
 */
class SearchBox(context: Context, attributeSet: AttributeSet) : CardView(context,
    attributeSet) {

  private val searchIcon: ImageView by lazy { findViewById<ImageView>(R.id.searchIcon) }
  private val searchField: EditText by lazy { findViewById<EditText>(R.id.searchInput) }
  private val overflowMenu: View by lazy { findViewById<View>(R.id.overflowMenu) }
  private val searchPlaceHolder by lazy { findViewById<View>(R.id.searchPlaceholder) }
  val textInputChanges: Observable<String>

  init {
    val inflater = LayoutInflater.from(context)
    inflater.inflate(R.layout.view_searchbox, this, true)
    useCompatPadding = true
    textInputChanges = RxTextView.textChanges(searchField).map { it.toString() }.share()

    /*
    val padding= dpToPx(R.dimen.activity_horizontal_margin).toInt()
    setPadding(padding, padding, padding, padding)
    clipToPadding = false
    */
  }

  var showInput: Boolean = false
    set(value) {
      field = value
      if (value) {
        searchField.visible()
        searchPlaceHolder.gone()
      } else {
        searchField.gone()
        searchPlaceHolder.visible()
      }
    }


  fun currentSearchText(): String = searchInput.text.toString()

  fun requestFocusForSearchInput(): Boolean {
    val r = searchField.requestFocus()
    searchField.showKeyboard()
    return r
  }

  fun animateSearchIconToCloseIcon() {
    val drawable = searchIcon.drawable as AnimatedVectorDrawable
    drawable.start()
  }

  fun hideKeyboard() {
    searchField.hideKeyboard()
  }

  fun showKeyboard() {
    searchField.requestFocus()
  }
}
