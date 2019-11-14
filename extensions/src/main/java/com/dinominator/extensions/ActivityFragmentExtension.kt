package com.dinominator.extensions

import android.annotation.TargetApi
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar

fun AppCompatActivity.replaceIfNotExisting(
    containerId: Int,
    fragment: Fragment,
    tag: String? = null,
    callback: ((Fragment) -> Unit)? = null
) {
    val existingFragment = supportFragmentManager.findFragmentByTag(tag)
    if (existingFragment == null) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment, tag)
            .runOnCommit {
                callback?.invoke(fragment)
            }
            .commitNow()
    } else {
        callback?.invoke(fragment)
    }
}

fun AppCompatActivity.replace(
    containerId: Int,
    fragment: Fragment,
    tag: String? = null,
    addToBackstack: Boolean = false,
    callback: ((Fragment) -> Unit)? = null,
    useSwipeToBack: Boolean = false
) {
    replace(supportFragmentManager, containerId, fragment, tag, addToBackstack, useSwipeToBack)
}

fun Fragment.replace(
    containerId: Int,
    fragment: Fragment,
    tag: String? = null,
    addToBackstack: Boolean = false,
    useSwipeToBack: Boolean = false
) {
    replace(fragmentManager, containerId, fragment, tag, addToBackstack, useSwipeToBack)
}

fun replace(
    fragmentManager: FragmentManager?,
    containerId: Int,
    fragment: Fragment,
    tag: String? = null,
    addToBackstack: Boolean = false,
    useSwipeToBack: Boolean = false
) {
    val fragmentTransaction =
        fragmentManager?.beginTransaction()

    fragmentTransaction?.setCustomAnimations(
        R.animator.slide_in_right,
        R.animator.fade_out,
        R.animator.fade_in,
        R.animator.slide_out_right
    )

    if (useSwipeToBack) {
        fragmentTransaction?.add(containerId, fragment, tag ?: fragment.getSimpleName())
    } else {
        fragmentTransaction?.replace(containerId, fragment, tag ?: fragment.getSimpleName())
    }
    fragmentTransaction?.let {
        if (addToBackstack) {
            it.addToBackStack(tag ?: fragment.getSimpleName())
        }
    }
    fragmentTransaction?.commit()
}

fun AppCompatActivity.add(
    containerId: Int,
    fragment: Fragment,
    tag: String? = null,
    addToBackstack: Boolean = false,
    callback: ((Fragment) -> Unit)? = null
) {
    add(supportFragmentManager, containerId, fragment, tag, addToBackstack)
}

fun Fragment.add(
    containerId: Int,
    fragment: Fragment,
    tag: String? = null,
    addToBackstack: Boolean = false
) {
    add(fragmentManager, containerId, fragment, tag, addToBackstack)
}

fun add(
    fragmentManager: FragmentManager?,
    containerId: Int,
    fragment: Fragment,
    tag: String? = null,
    addToBackstack: Boolean = false
) {
    val fragmentTransaction =
        fragmentManager?.beginTransaction()

    fragmentTransaction?.setCustomAnimations(
        R.animator.slide_in_right,
        R.animator.fade_out,
        R.animator.fade_in,
        R.animator.slide_out_right
    )

    fragmentTransaction?.add(containerId, fragment, tag ?: fragment.getSimpleName())
    fragmentTransaction?.let {
        if (addToBackstack) {
            it.addToBackStack(tag ?: fragment.getSimpleName())
        }
    }
    fragmentTransaction?.commit()
}

fun AppCompatActivity.reload() {
    val frg = supportFragmentManager.findFragmentById(R.id.container)
    val ft = supportFragmentManager.beginTransaction()
    frg?.let {
        ft.detach(it)
        ft.attach(it)
        ft.commit()
    }
}

fun getTopFragment(manager: FragmentManager?): Fragment? {
    if (manager != null) {
        if (manager.backStackEntryCount > 0) {
            val backStackEntry = manager.getBackStackEntryAt(manager.backStackEntryCount - 1)
            return manager.findFragmentByTag(backStackEntry.name)
        } else {
            val fragments = manager.fragments
            if (fragments.size > 0) {
                return fragments[0]
            }
        }
    }
    return null
}

fun Fragment.clearFragmentBackstack() {
    val manager = activity?.supportFragmentManager
    manager?.let {
        while (manager.backStackEntryCount > 0)
            manager.popBackStackImmediate()
    }
}

fun Context.getColorAttr(attr: Int): Int {
    val theme = theme
    val typedArray = theme.obtainStyledAttributes(intArrayOf(attr))
    val color = typedArray.getColor(0, Color.LTGRAY)
    typedArray.recycle()
    return color
}

fun Context.getColorCompat(id: Int): Int = ContextCompat.getColor(this, id)

fun Context.getDrawableCompat(id: Int): Drawable? = ContextCompat.getDrawable(this, id)

fun Context.formatStringResource(resId: Int, param: String?): String? = String.format(this.getString(resId), param)

fun FragmentActivity.clearDarkStatusBarIcons() {
    this.window?.decorView?.let { decoder ->
        decoder.systemUiVisibility = decoder.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun FragmentActivity.setStatusBarColor(colorAttr: Int = R.attr.colorAccent) {
    window?.statusBarColor = getColorAttr(colorAttr)
}

fun Fragment.getDrawable(@DrawableRes drawableRes: Int): Drawable? =
    ContextCompat.getDrawable(requireContext(), drawableRes)

fun Context.isConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnected == true
}

fun Activity.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}


fun Snackbar.materialize(drawable: Int? = null): Snackbar {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    val margin = context.resources.getDimensionPixelSize(R.dimen.spacing_xs_large)
    params.setMargins(margin, margin, margin, 12)
    this.view.layoutParams = params
    this.view.background = context.getDrawableCompat(drawable ?: R.drawable.snackbar_background)
    ViewCompat.setElevation(this.view, 6f)
    return this
}

fun Fragment.isConnected(): Boolean {
    val isConnected = context?.isConnected() ?: false
    if (!isConnected) {
        view?.let {
            com.google.android.material.snackbar.Snackbar.make(
                it,
                getString(R.string.label_no_internet_connection),
                Snackbar.LENGTH_LONG
            )
                .materialize()
                .show()
        }
    }
    return isConnected
}

fun Int.toPx(): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (this * density + .5F).toInt()
}

fun getCustomProduct(): String {
    val fingerprint = Build.FINGERPRINT
    val items = fingerprint.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    return items[1]
}

fun Context.enableGPS(status: Boolean) {
    Settings.Secure.setLocationProviderEnabled(applicationContext.contentResolver, LocationManager.GPS_PROVIDER, status)
}

fun Context.getGPSStatus(): Boolean {
    return Settings.Secure.isLocationProviderEnabled(applicationContext.contentResolver, LocationManager.GPS_PROVIDER)
}

fun Context.clearNotification(id: Int) {
    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(id)
}