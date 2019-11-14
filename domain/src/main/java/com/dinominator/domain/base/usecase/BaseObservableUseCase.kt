package com.dinominator.domain.base.usecase

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseObservableUseCase : BaseUseCase() {
    abstract override fun buildObservable(): Observable<*>

    fun disposeAndExecuteObservable(observer: Observer<Any>) {
        disposeAndExecute(buildObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { observer.onSubscribe(it) }
            .subscribe
                (
                { observer.onNext(it) },
                { observer.onError(it) },
                { observer.onComplete() }
            ))
    }

    fun executeObservable(observer: Observer<Any>) {
        execute(buildObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { observer.onSubscribe(it) }
            .subscribe
                (
                { observer.onNext(it) },
                { observer.onError(it) },
                { observer.onComplete() }
            ))
    }

    fun <T> executeSafely(observable: Observable<T>) {
        execute(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { t -> t.printStackTrace() })
        )
    }
}
