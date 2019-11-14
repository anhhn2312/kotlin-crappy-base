package com.dinominator.kotlin_awesome_app.ui.widget.recyclerview.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private val spaceLeft: Int,
    private val spaceTop: Int,
    private val spaceRight: Int,
    private val spaceBottom: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceTop
            }
            left = spaceLeft
            right = spaceRight
            bottom = spaceBottom
        }
    }
}