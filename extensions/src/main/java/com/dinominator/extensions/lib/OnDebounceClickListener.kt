package com.dinominator.extensions.lib

import android.view.View
import timber.log.Timber

abstract class OnDebounceClickListener : View.OnClickListener {

    private var mLastClickTime: Long = 0

    override fun onClick(v: View) {
        val lastClickTime = mLastClickTime
        val now = System.currentTimeMillis()
        mLastClickTime = now
        if (now - lastClickTime < MIN_DELAY_MS) {
            // Too fast: ignore
            Timber.d("clicked too quickly: ignored")
        } else {
            // Register click
            onDebounceClicked(v)
        }
    }

    abstract fun onDebounceClicked(v: View)

    companion object {
        private const val MIN_DELAY_MS: Long = 600

        fun wrap(onClick: (v: View) -> Unit): OnDebounceClickListener {
            return object : OnDebounceClickListener() {
                override fun onDebounceClicked(v: View) {
                    onClick(v)
                }
            }
        }
    }
}