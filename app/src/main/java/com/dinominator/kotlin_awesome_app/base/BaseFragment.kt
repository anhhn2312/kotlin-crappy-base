package com.dinominator.kotlin_awesome_app.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.dinominator.extensions.*
import com.dinominator.kotlin_awesome_app.R
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment<T: ViewDataBinding> : DaggerFragment(), ActivityCallback {

    private var activityCallback: ActivityCallback? = null

    private val progressBar by lazy {
        MaterialDialog(activity!!).apply {
            window?.setBackgroundDrawableResource(R.color.transparent)
            setContentView(R.layout.progress_layout)
            setCancelable(false)
        }
    }

    lateinit var viewBinding: T

    @LayoutRes
    abstract fun layoutRes(): Int

    abstract fun onFragmentCreated(view: View, savedInstanceState: Bundle?)
    abstract fun viewModel(): BaseViewModel?

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCallback = context as? ActivityCallback
    }

    override fun onDetach() {
        activityCallback = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewBinding = DataBindingUtil.inflate(inflater, layoutRes(), container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onFragmentCreated(view, savedInstanceState)

        val swipeRefresh = this@BaseFragment.view?.findViewById<SwipeRefreshLayout?>(R.id.swipeRefresh)
        swipeRefresh?.setColorSchemeColors(context!!.getColorAttr(R.attr.colorAccent))

        viewModel()?.progress?.observeNull(viewLifecycleOwner) {
            swipeRefresh?.isRefreshing = ((it != null) && (it == true))
        }

        viewModel()?.error?.observeNotNull(viewLifecycleOwner) {
            if (isConnected()) {
                this@BaseFragment.view?.let { view -> showSnackBar(view, resId = it.resId, message = it.message) }
            }
        }

        viewModel()?.progressBar?.observeNotNull(viewLifecycleOwner) {
            if (it) progressBar.show()
            else progressBar.dismiss()
        }

        //prevent user from interacting with the below fragment in the backstack
        //when using addFragment() method
        view.setOnTouchListener { _, _ -> true }
    }

    override fun onSnackbarDismiss() {
    }

    override fun showSnackBar(
        root: View,
        resId: Int?,
        message: String?,
        duration: Int,
        actionText: Int?,
        action: View.OnClickListener?,
        callback: ActivityCallback?
    ) {
        activityCallback?.showSnackBar(root, resId, message, duration, actionText, action, callback)
    }

    open fun onBackPressed(): Boolean {
        activity?.onBackPressed()
        return true
    }

    fun setupToolbar(
        titleStr: String? = null,
        titleResId: Int,
        menuId: Int? = null,
        onMenuItemClick: ((item: MenuItem) -> Unit)? = null,
        hideNavigation: Boolean = false
    ) {
        val title = titleStr ?: getString(titleResId)
        view?.findViewById<Toolbar?>(R.id.toolbar)?.apply {
            if (hideNavigation) navigationIcon = null
            val titleText = findViewById<TextView?>(R.id.toolbarTitle)
            if (titleText != null) {
                titleText.text = title
            } else {
                setTitle(title)
            }
            setNavigationOnClickListener {
                onBackPressed()
            }
            menuId?.let { menuResId ->
                inflateMenu(menuResId)
                onMenuItemClick?.let { onClick ->
                    setOnMenuItemClickListener {
                        onClick.invoke(it)
                        return@setOnMenuItemClickListener true
                    }
                }
            }
            return
        }
        //if no toolbar is found in fragment, request parent activity to update it's toolbar (if it has any)
        (activity as? BaseActivity)?.setupToolbar(title, menuId, onMenuItemClick, hideNavigation)
    }

    /**
     * Called on the top fragment in the backstack
     * when it is just added to the backstack or its above fragment is poped out from the backstack
     */
    open fun onDisplay() {
        view?.hideKeyboard()
    }
}