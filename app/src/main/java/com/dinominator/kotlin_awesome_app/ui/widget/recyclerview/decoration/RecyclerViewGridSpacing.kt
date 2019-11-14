package com.dinominator.kotlin_awesome_app.ui.widget.recyclerview.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dinominator.kotlin_awesome_app.R

class RecyclerViewGridSpacing(
    context: Context,
    private val spanCount: Int,
    spacingDimen: Int = R.dimen.grid_spacing,
    private val includeEdge: Boolean = false
) : RecyclerView.ItemDecoration() {

    private val spacing = context.resources.getDimensionPixelSize(spacingDimen)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount

            if (position < spanCount) {
                outRect.top = spacing
            }
            outRect.bottom = spacing
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing
            }
        }
    }
}
