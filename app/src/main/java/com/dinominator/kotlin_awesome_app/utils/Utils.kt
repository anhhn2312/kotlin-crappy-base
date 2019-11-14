package com.dinominator.kotlin_awesome_app.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.Browser
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.os.bundleOf
import com.airbnb.deeplinkdispatch.DeepLink
import com.dinominator.extensions.getColorAttr
import com.dinominator.kotlin_awesome_app.R
import com.dinominator.resources.NetworkConfig.AUTHORIZATION
import com.dinominator.resources.NetworkConfig.BEARER
import java.security.KeyStore

fun Activity.fromDeepLink() = intent?.getBooleanExtra(DeepLink.IS_DEEP_LINK, false) ?: false


private fun String.newEmptyKeyStore(): KeyStore = kotlin.runCatching {
    val keystore = KeyStore.getInstance(KeyStore.getDefaultType())
    keystore.load(null, toCharArray())
    keystore
}.getOrThrow()

fun Context.openInBrowser(data: String?, token: String) {
    val tabIntent = CustomTabsIntent.Builder()
        .setToolbarColor(getColorAttr(R.attr.colorPrimary))
        .enableUrlBarHiding()
        .build()

    tabIntent.intent.putExtra(Browser.EXTRA_HEADERS, bundleOf(AUTHORIZATION to "$BEARER $token"))

    runCatching {
        data?.let {
            tabIntent.launchUrl(this, Uri.parse(data).normalizeScheme())
        }
    }
}