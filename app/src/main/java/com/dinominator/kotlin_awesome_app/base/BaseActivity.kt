package com.dinominator.kotlin_awesome_app.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dinominator.data.storage.AppSharedPreference
import com.dinominator.data.storage.theme
import com.dinominator.domain.repository.services.AuthenticationService
import com.dinominator.extensions.getColorAttr
import com.dinominator.extensions.getTopFragment
import com.dinominator.extensions.isConnected
import com.dinominator.extensions.materialize
import com.dinominator.kotlin_awesome_app.R
import com.dinominator.kotlin_awesome_app.base.engine.ThemeEngine
import com.dinominator.kotlin_awesome_app.platform.http.AutoAuthenticator
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), ActivityCallback, FragmentManager.OnBackStackChangedListener {

    @Inject
    lateinit var preference: AppSharedPreference
    @Inject
    lateinit var autoRefreshToken: AutoAuthenticator
    @Inject
    lateinit var authenService: AuthenticationService

    private var networkStateChangeReceiver: BroadcastReceiver? = null
    var isInternetConnected: Boolean = false

    @LayoutRes
    abstract fun layoutRes(): Int

    abstract fun onActivityCreated(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this) // for the sake of theme engine
        ThemeEngine.setTheme(this, preference.theme)
        super.onCreate(savedInstanceState)
        val layoutRes = layoutRes()
        if (layoutRes > 0) setContentView(layoutRes)

        autoRefreshToken.prefs = preference
        autoRefreshToken.service = authenService

        Timber.i("Authentication: $authenService prefs: $preference: ")

        val refresh = this@BaseActivity.findViewById<SwipeRefreshLayout?>(R.id.swipeRefresh)
        refresh?.setColorSchemeColors(getColorAttr(R.attr.colorAccent))

        networkStateChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    isInternetConnected = context?.isConnected() ?: false
                    onNetWorkStateChange(isInternetConnected)
                }
            }
        }

        applicationContext.registerReceiver(
            networkStateChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        if (savedInstanceState == null) {
            onActivityCreated(savedInstanceState)
        }

        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onDestroy() {
        applicationContext.unregisterReceiver(networkStateChangeReceiver)
        networkStateChangeReceiver = null
        supportFragmentManager.removeOnBackStackChangedListener(this)
        super.onDestroy()
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
        if ((resId == null && message == null) || isFinishing) return

        val snackBar = Snackbar.make(root, message ?: getString(resId ?: R.string.label_no_data), duration)

        actionText?.let { snackBar.setAction(it, action) }

        (snackBar.view.findViewById(R.id.snackbar_text) as? TextView)?.maxLines = 3

        snackBar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                callback?.onSnackbarDismiss()
            }
        })

        snackBar.materialize().show()
    }

    override fun onSnackbarDismiss() {}

    protected open fun onNetWorkStateChange(isConnected: Boolean) = Unit

    fun setupToolbar(
        title: String?,
        menuId: Int? = null,
        onMenuItemClick: ((item: MenuItem) -> Unit)? = null,
        hideNavigation: Boolean = false
    ) {
        findViewById<Toolbar?>(R.id.toolbar)?.apply {
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
        }
    }

    fun setupToolbar(
        resId: Int,
        menuId: Int? = null,
        onMenuItemClick: ((item: MenuItem) -> Unit)? = null,
        hideNavigation: Boolean = false
    ) {
        setupToolbar(getString(resId), menuId, onMenuItemClick, hideNavigation)
    }

    override fun onBackStackChanged() {
        val fragment = getTopFragment(supportFragmentManager)
        if (fragment is BaseFragment<*>) {
            fragment.onDisplay()
        }
    }
}

interface ActivityCallback {

    fun onSnackbarDismiss()

    fun showSnackBar(
        root: View, resId: Int? = null, message: String? = null, duration: Int = Snackbar.LENGTH_LONG,
        actionText: Int? = null, action: View.OnClickListener? = null, callback: ActivityCallback? = null
    )
}