package com.dinominator.kotlin_awesome_app.platform.deeplink

import com.airbnb.deeplinkdispatch.DeepLinkSpec
import com.dinominator.resources.DeepLinkConfig.IN_APP_LINK

@DeepLinkSpec(prefix = [IN_APP_LINK])
annotation class AppDeepLink(vararg val value: String)
