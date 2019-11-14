package com.dinominator.domain.request

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import timber.log.Timber

class BaseRequest<E>(@SerializedName("data") var data: String) {
    constructor(e: E) : this(Gson().toJson(e)) {
        Timber.tag("request data: ").d(data)
    }
}