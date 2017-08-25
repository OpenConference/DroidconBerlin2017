package de.droidcon.berlin2017.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * An image view with square aspect ratio (respecting width).
 * @author Hannes Dorfmann
 */
class SquareImageView : ImageView {
  constructor(context: Context) : super(context) {
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    setMeasuredDimension(measuredWidth, measuredWidth)
  }
}
