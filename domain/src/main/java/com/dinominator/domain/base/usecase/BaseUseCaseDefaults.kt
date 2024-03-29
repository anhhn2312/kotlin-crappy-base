package com.dinominator.domain.base.usecase

import io.reactivex.*

interface BaseUseCaseDefault {
    fun buildObservable(): Observable<*>? = null
    fun buildSingle(): Single<*>? = null
    fun buildFlowable(): Flowable<*>? = null
    fun buildMaybe(): Maybe<*>? = null
    fun buildCompletable(): Completable? = null
}