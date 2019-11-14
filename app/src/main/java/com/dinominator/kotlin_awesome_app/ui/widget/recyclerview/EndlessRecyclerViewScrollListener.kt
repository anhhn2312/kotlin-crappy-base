package com.dinominator.kotlin_awesome_app.ui.widget.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class EndlessRecyclerViewScrollListener(private val onLoadMore: (totalCount: Int) -> Unit) :
    RecyclerView.OnScrollListener() {
    private var visibleThreshold = 5
    private var previousTotalItemCount = 0
    private var loading = true
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var firstAttach = true

    private fun init(recyclerView: RecyclerView) {
        this.layoutManager = recyclerView.layoutManager
        when (val lm = layoutManager ?: return) {
            is GridLayoutManager -> init(lm)
            is StaggeredGridLayoutManager -> init(lm)
        }
    }

    private fun init(layoutManager: GridLayoutManager) {
        this.layoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    private fun init(layoutManager: StaggeredGridLayoutManager) {
        this.layoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    private fun getFirstVisibleItemStaggeredLayout(firstVisibleItemPositions: IntArray): Int {
        var minSize = 0
        for (i in firstVisibleItemPositions.indices) {
            if (i == 0) {
                minSize = firstVisibleItemPositions[i]
            } else if (firstVisibleItemPositions[i] < minSize) {
                minSize = firstVisibleItemPositions[i]
            }
        }
        return minSize
    }

    fun getFirstVisibleItem(recyclerView: RecyclerView?): Int {
        if (recyclerView == null) return -1
        if (layoutManager == null) init(recyclerView)
        val layoutManager = layoutManager ?: return -1

        var firstVisibleItemPosition = 0
        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val firstVisibleItemPositions = layoutManager.findFirstVisibleItemPositions(null)
                firstVisibleItemPosition = getFirstVisibleItemStaggeredLayout(firstVisibleItemPositions)
            }
            is GridLayoutManager -> firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            is LinearLayoutManager -> firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        }
        return firstVisibleItemPosition
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        if (firstAttach) {
            firstAttach = false
            return
        }

        if (layoutManager == null) init(view)

        val layoutManager = layoutManager ?: return
        val totalItemCount = layoutManager.itemCount
        var lastVisibleItemPosition = 0

        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            is LinearLayoutManager -> lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        }

        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            onLoadMore.invoke(totalItemCount)
            loading = true
        }
    }

    fun resetState() {
        this.previousTotalItemCount = 0
        this.loading = true
    }

}