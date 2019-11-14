package com.dinominator.kotlin_awesome_app.platform.fabric

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.dinominator.kotlin_awesome_app.BuildConfig
import io.fabric.sdk.android.Fabric

object FabricProvider {

    fun initFabric(context: Context) {
        val fabric = Fabric.Builder(context)
            .kits(
                Crashlytics.Builder()
                    .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                    .build()
            )
            .debuggable(BuildConfig.DEBUG)
            .build()
        Fabric.with(fabric)
    }
}
