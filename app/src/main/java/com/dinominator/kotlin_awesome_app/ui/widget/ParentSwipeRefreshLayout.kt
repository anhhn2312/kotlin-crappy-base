package com.dinominator.kotlin_awesome_app.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dinominator.extensions.getColorAttr
import com.dinominator.kotlin_awesome_app.R
import com.google.android.material.appbar.AppBarLayout

class ParentSwipeRefreshLayout : SwipeRefreshLayout, AppBarLayout.OnOffsetChangedListener {
    private var appBarLayout: AppBarLayout? = null
        set(value) {
            field = value
            value?.addOnOffsetChangedListener(this)
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setColorSchemeColors(context!!.getColorAttr(R.attr.colorAccent))
    }

    override fun onDetachedFromWindow() {
        appBarLayout?.removeOnOffsetChangedListener(this)
        appBarLayout = null
        super.onDetachedFromWindow()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        this.isEnabled = i == 0
    }
}