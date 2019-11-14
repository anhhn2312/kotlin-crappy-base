package com.dinominator.kotlin_awesome_app.platform.deeplink

import com.airbnb.deeplinkdispatch.DeepLinkSpec

@DeepLinkSpec(prefix = ["https://kotlinawesome.app"])
annotation class WebDeepLink(vararg val value: String)