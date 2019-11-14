package com.dinominator.kotlin_awesome_app.ui.widget.recyclerview.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dinominator.kotlin_awesome_app.R

class RecyclerViewHorizontalSpacing(val context: Context, spacingDimen: Int = R.dimen.spacing_xs_large) :
    RecyclerView.ItemDecoration() {
    private val mPadding: Int = context.resources.getDimensionPixelSize(spacingDimen)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }
        outRect.left = mPadding
        if (itemPosition == parent.adapter?.itemCount?.minus(1))
            outRect.right = mPadding
    }
}
