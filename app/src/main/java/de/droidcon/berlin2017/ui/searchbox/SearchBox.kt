package de.droidcon.berlin2017.ui.searchbox

import android.animation.Animator
import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.support.v7.widget.CardView
import android.support.v7.widget.ListPopupWindow
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import com.jakewharton.rxbinding2.widget.RxTextView
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.gone
import de.droidcon.berlin2017.ui.hideKeyboard
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
  var overflowMenuClickListener: ((Int) -> Unit)? = null

  init {
    val inflater = LayoutInflater.from(context)
    inflater.inflate(R.layout.view_searchbox, this, true)
    useCompatPadding = true
    textInputChanges = RxTextView.textChanges(searchField).map { it.toString() }.share()
    overflowMenu.setOnClickListener {
      val adapter = ArrayAdapter<String>(context,
          android.R.layout.simple_list_item_1,
          context.resources.getStringArray(R.array.overflow_items))

      val popup = ListPopupWindow(context)
      popup.anchorView = overflowMenu
      popup.setAdapter(adapter)
      popup.width = context.resources.getDimensionPixelOffset(R.dimen.overflow_popup_width)
      popup.setOnItemClickListener { adapterView, view, i, l ->
        overflowMenuClickListener?.invoke(i)
      }
      popup.show()
    }

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

  fun requestFocusForSearchInput() {
    /*
    Handler(Looper.getMainLooper()).post {
      // searchField.showKeyboard()
    }
    */
    searchField.setFocusableInTouchMode(true)
    searchField.requestFocus()
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

  fun slideInIfNeeded() {
    if (translationY != 0f) {
      val animator = animate().translationY(0f).setInterpolator(
          OvershootInterpolator()).setDuration(
          300).setStartDelay(200)

      animator.setListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animator: Animator) {
          visible()
        }

        override fun onAnimationEnd(animator: Animator) {}
        override fun onAnimationCancel(animator: Animator) {}

        override fun onAnimationRepeat(animator: Animator) {}
      })

      animator.start()
    }

  }
}
