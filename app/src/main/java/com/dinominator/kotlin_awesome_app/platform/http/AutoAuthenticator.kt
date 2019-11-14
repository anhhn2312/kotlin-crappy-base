package com.dinominator.kotlin_awesome_app.platform.http

import com.dinominator.data.storage.AppSharedPreference
import com.dinominator.data.storage.token
import com.dinominator.domain.repository.services.AuthenticationService
import com.dinominator.resources.NetworkConfig.AUTHORIZATION
import com.dinominator.resources.NetworkConfig.BEARER
import com.dinominator.resources.NetworkConfig.RETRY_COUNT
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber

class AutoAuthenticator constructor(
    var prefs: AppSharedPreference? = null,
    var service: AuthenticationService? = null
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (prefs == null || service == null) return null

        val oldToken: String? = try {
            response.request().header(AUTHORIZATION)?.split(" ")?.get(1)
        } catch (e: Exception) {
            ""
        }

        if (prefs?.token != oldToken) return null
        if (responseCount(response) >= RETRY_COUNT) return null

        // request new key here
        var newToken = "dummy_value"
        Timber.i("New Token: $newToken")

        return if (newToken.isNotEmpty()) {
            // retry the failed 401 request with new access token
            response.request().newBuilder()
                .header(AUTHORIZATION, "$BEARER $newToken") // use the new access token
                .build()
        } else null
    }

    private fun responseCount(res: Response?): Int {
        var response = res
        var result = 1
        while (response != null) {
            response = response.priorResponse()
            result++
        }
        return result
    }
}