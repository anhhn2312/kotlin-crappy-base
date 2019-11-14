package com.dinominator.data.storage


inline var AppSharedPreference.token: String
    get() = this.getString("token") ?: ""
    set(value) = this.set("token", value)

inline var AppSharedPreference.fcmToken: String
    get() = this.getString("fcm_token") ?: ""
    set(value) = this.set("fcm_token", value)

inline var AppSharedPreference.theme: Int
    get() = this.getInt("appTheme", 1)
    set(value) = this.set("appTheme", value)

inline var AppSharedPreference.languageTag: String
    get() = this.getString("app_language_tag", "en-US") ?: "en-US"
    set(value) = this.set("app_language_tag", value)