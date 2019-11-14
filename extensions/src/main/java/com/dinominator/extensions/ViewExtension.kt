package com.dinominator.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dinominator.extensions.lib.OnDebounceClickListener

fun ViewGroup.inflater(@LayoutRes id: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(id, this, attachToRoot)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.setOnDebounceClick(onClick: (v: View) -> Unit) {
    this.setOnClickListener(OnDebounceClickListener.wrap(onClick))
}

fun RecyclerView.Adapter<*>.observeData(event: () -> Unit) {
    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            event.invoke()
            unregisterAdapterDataObserver(this)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            event.invoke()
            unregisterAdapterDataObserver(this)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            event.invoke()
            unregisterAdapterDataObserver(this)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            event.invoke()
            unregisterAdapterDataObserver(this)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            event.invoke()
            unregisterAdapterDataObserver(this)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            event.invoke()
            unregisterAdapterDataObserver(this)
        }
    })
}