package com.dinominator.kotlin_awesome_app

import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.Configuration
import androidx.work.WorkManager
import com.airbnb.deeplinkdispatch.DeepLinkHandler
import com.dinominator.kotlin_awesome_app.di.components.AppComponent
import com.dinominator.kotlin_awesome_app.platform.fabric.FabricProvider
import com.dinominator.kotlin_awesome_app.platform.receiver.DeepLinkReceiver
import com.dinominator.kotlin_awesome_app.platform.timber.AppTree
import com.dinominator.kotlin_awesome_app.platform.timber.FabricTree
import com.evernote.android.state.StateSaver
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class KotlinAwesomeApp : DaggerApplication() {
    private val appComponent by lazy { AppComponent.getComponent(this) }

    private var deepLinkReceiver: DeepLinkReceiver? = null

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = appComponent

    override fun onCreate() {
        super.onCreate()
        initConfigs()
    }

    private fun initConfigs() {
        WorkManager.initialize(
            this,
            Configuration.Builder().setWorkerFactory(appComponent.daggerWorkerFactory()).build()
        )

        StateSaver.setEnabledForAllActivitiesAndSupportFragments(this, true)
        if (BuildConfig.DEBUG) {
            Timber.plant(AppTree())
        } else {
            FabricProvider.initFabric(this)
            Timber.plant(FabricTree())
        }

        deepLinkReceiver = DeepLinkReceiver()
        deepLinkReceiver?.let {
            LocalBroadcastManager.getInstance(this)
                .registerReceiver(it, IntentFilter(DeepLinkHandler.ACTION))
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        deepLinkReceiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
        }
    }
}