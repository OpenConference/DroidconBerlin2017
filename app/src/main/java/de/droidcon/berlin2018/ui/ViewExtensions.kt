package de.droidcon.berlin2018.ui

import android.content.Context
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.inputmethod.InputMethodManager


fun View.gone() {
  visibility = View.GONE
}

fun View.visible() {
  visibility = View.VISIBLE
}

fun Context.dpToPx(dp: Float): Float {
  val density = resources.displayMetrics.density
  return dp * density
}

fun Context.dpToPx(@DimenRes dimId: Int) = dpToPx(resources.getDimensionPixelOffset(dimId).toFloat())

fun View.dpToPx(dp: Float) = context.dpToPx(dp)

fun View.dpToPx(@DimenRes dimId: Int) = context.dpToPx(dimId)

fun ViewHolder.dpToPx(@DimenRes dimId: Int) = itemView.dpToPx(dimId)


fun View.hideKeyboard() {
  (context.getSystemService(
      Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
      windowToken, 0)
}

fun View.showKeyboard() {
  (context.getSystemService(
      Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
      InputMethodManager.SHOW_FORCED, 0)
}
