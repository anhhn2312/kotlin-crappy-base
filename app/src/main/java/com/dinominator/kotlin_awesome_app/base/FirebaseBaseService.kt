package com.dinominator.kotlin_awesome_app.base

import com.google.firebase.messaging.FirebaseMessagingService
import dagger.android.AndroidInjection

abstract class FirebaseBaseService : FirebaseMessagingService() {
    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }
}