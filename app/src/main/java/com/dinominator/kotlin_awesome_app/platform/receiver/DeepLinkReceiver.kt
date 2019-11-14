package com.dinominator.kotlin_awesome_app.platform.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import com.dinominator.extensions.TAG
import timber.log.Timber

class DeepLinkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val deepLinkUri = it.getStringExtra(DeepLinkHandler.EXTRA_URI)
            if (it.getBooleanExtra(DeepLinkHandler.EXTRA_SUCCESSFUL, false)) {
                Timber.i(TAG, "Success deep linking: $deepLinkUri")
            } else {
                val errorMessage = it.getStringExtra(DeepLinkHandler.EXTRA_ERROR_MESSAGE)
                Timber.e(TAG, "Error deep linking: $deepLinkUri with error message + $errorMessage")
            }
        }
    }

    companion object
}