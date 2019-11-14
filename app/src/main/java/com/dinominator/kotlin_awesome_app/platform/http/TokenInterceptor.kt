package com.dinominator.kotlin_awesome_app.platform.http

import android.provider.Telephony.Carriers.BEARER
import com.dinominator.data.storage.AppSharedPreference
import com.dinominator.data.storage.languageTag
import com.dinominator.data.storage.token
import com.dinominator.resources.NetworkConfig.ACCEPT_LANGUAGE
import com.dinominator.resources.NetworkConfig.AUTHORIZATION
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(private val prefs: AppSharedPreference) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.header(AUTHORIZATION, "$BEARER ${prefs.token}")
        builder.header(ACCEPT_LANGUAGE, prefs.languageTag)
        return chain.proceed(builder.build())
    }
}