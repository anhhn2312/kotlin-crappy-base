package com.dinominator.domain.request

import com.google.gson.annotations.SerializedName

data class AuthenticationRequest(
    @SerializedName("imei") var imei: Long,
    @SerializedName("serial") var serial: String,
    @SerializedName("macAddress") var macAddress: String,
    @SerializedName("country") var country: String,
    @SerializedName("versionDevice") var versionDevice: String,
    @SerializedName("fcmToken") var fcmToken: String,
    @SerializedName("modelName") var modelName: String,
    @SerializedName("versionApp") var versionApp: String
)