package com.dinominator.kotlin_awesome_app.ui.widget.recyclerview

import android.content.Context
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.recyclerview.widget.*
import com.dinominator.extensions.getColorAttr
import com.dinominator.kotlin_awesome_app.R
import com.dinominator.kotlin_awesome_app.ui.widget.recyclerview.decoration.InsetDividerDecoration
import com.dinominator.kotlin_awesome_app.ui.widget.recyclerview.decoration.RecyclerViewHorizontalSpacing

class BaseRecyclerView constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var emptyView: View? = null
    private var parentView: View? = null
    private var onLoadMore: (() -> Unit)? = null
    private var onScrollToTop: (() -> Unit)? = null
    private var onAdapterLoadFinish: ((loadNewItemVisible: Boolean, needScroll: Boolean) -> Unit)? = null
    private var preItemCount = -1
    private val onScrollChangedListener = ViewTreeObserver.OnScrollChangedListener {
        if (computeVerticalScrollOffset() == 0) {
            onScrollToTop?.invoke()
        }
    }

    private val onScrollMore = EndlessRecyclerViewScrollListener { if (it >= PAGE_SIZE) onLoadMore?.invoke() }

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            showEmptyView()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            showEmptyView(positionStart == 0)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            showEmptyView(positionStart == 0)
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        super.setAdapter(adapter)
        if (isInEditMode) return
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer)
            observer.onChanged()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        runCatching {
            removeOnScrollListener(onScrollMore)
            viewTreeObserver?.removeOnScrollChangedListener(onScrollChangedListener)
        }
    }

    fun setEmptyView(emptyView: View, parentView: View? = null) {
        this.emptyView = emptyView
        this.parentView = parentView
    }

    fun addOnLoadMore(onLoadMore: (() -> Unit)? = null) {
        this.onLoadMore = onLoadMore
        if (onLoadMore == null) {
            removeOnScrollListener(onScrollMore)
        } else {
            addOnScrollListener(onScrollMore)
        }
    }

    fun addOnAdapterLoadFinish(onAdapterLoadFinish: ((loadNewItemVisible: Boolean, needScroll: Boolean) -> Unit)? = null) {
        this.onAdapterLoadFinish = onAdapterLoadFinish
    }

    fun resetScrollState() {
        onScrollMore.resetState()
    }

    fun addOnScrollToTop(onScrollToTop: () -> Unit) {
        this.onScrollToTop = onScrollToTop
        viewTreeObserver?.removeOnScrollChangedListener(onScrollChangedListener)
        viewTreeObserver?.addOnScrollChangedListener(onScrollChangedListener)
    }

    private fun showEmptyView(handleShowNewItem: Boolean = true) {
        val adapter = adapter
        if (adapter != null) {
            if (emptyView != null) {
                if (adapter.itemCount == 0) {
                    onScrollMore.resetState()
                    showParentOrSelf(false)
                } else {
                    showParentOrSelf(true)
                }
            }

            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (handleShowNewItem) {
                        val firstItem = onScrollMore.getFirstVisibleItem(this@BaseRecyclerView)
                        onAdapterLoadFinish?.invoke(
                            preItemCount < adapter.itemCount && computeVerticalScrollOffset() > 0,
                            firstItem < 5
                        )
                        preItemCount = adapter.itemCount
                    }
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        } else {
            if (emptyView != null) {
                showParentOrSelf(false)
            }
        }
    }

    private fun showParentOrSelf(showRecyclerView: Boolean) {
        parentView?.isVisible = parentView != null
        isVisible = true
        emptyView?.isVisible = !showRecyclerView
    }

    var smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return if (displayMetrics != null) {
                50f / displayMetrics.densityDpi
            } else super.calculateSpeedPerPixel(displayMetrics)
        }
    }

    fun startSmoothScroll(position: Int) {
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }
}

fun RecyclerView.addDivider(
    dividerHeight: Int = R.dimen.divider_height,
    dividerColorAttr: Int = R.attr.dividerColor,
    insetLeft: Int = R.dimen.default_divider_inset,
    insetRight: Int = R.dimen.default_divider_inset
) {
    if (canAddDivider()) {
        val resources = resources
        addItemDecoration(
            InsetDividerDecoration(
                resources.getDimensionPixelSize(dividerHeight),
                resources.getDimensionPixelSize(insetLeft),
                resources.getDimensionPixelSize(insetRight),
                context.getColorAttr(dividerColorAttr)
            )
        )
    }
}

fun RecyclerView.addHorizontalSpacing(spacingDimen: Int = R.dimen.spacing_xs_large) {
    addItemDecoration(
        RecyclerViewHorizontalSpacing(context, spacingDimen)
    )
}

fun RecyclerView.canAddDivider(): Boolean {
    if (layoutManager != null) {
        when (val layoutManager = this.layoutManager) {
            is GridLayoutManager -> return layoutManager.spanCount == 1
            is LinearLayoutManager -> return true
            is StaggeredGridLayoutManager -> return layoutManager.spanCount == 1
        }
    }
    return false
}
