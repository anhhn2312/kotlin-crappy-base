package com.dinominator.domain.repository.services

import com.dinominator.domain.request.AuthenticationRequest
import com.dinominator.domain.request.BaseRequest
import com.dinominator.domain.response.BaseResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {

    @POST("warranty/auth")
    fun authenticate(@Body request: BaseRequest<AuthenticationRequest>): Observable<BaseResponse<*>>

}