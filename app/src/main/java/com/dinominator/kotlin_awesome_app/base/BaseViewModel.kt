package com.dinominator.kotlin_awesome_app.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dinominator.data.models.AppErrors
import com.dinominator.extensions.uiThread
import com.dinominator.kotlin_awesome_app.R
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    @Inject
    lateinit var gson: Gson
    val error = MutableLiveData<AppErrors>()
    val progress = MutableLiveData<Boolean>()
    val progressBar = MutableLiveData<Boolean>()
    private val disposable = CompositeDisposable()

    protected fun add(disposable: Disposable) = this.disposable.add(disposable)

    protected fun disposeAll() = disposable.clear()

    private fun handleError(throwable: Throwable) {
        hideProgress()
        if (throwable is HttpException) {
            val response = throwable.response()
            try {
                val message = JSONObject(response.errorBody()?.string() ?: "").getString("message")
                val code = response.code()
                if (code == 401) { //Unauthorized
                    error.postValue(
                        AppErrors(
                            AppErrors.ErrorType.OTHER,
                            resId = R.string.label_network_error
                        )
                    )
                } else {
                    error.postValue(
                        AppErrors(
                            AppErrors.ErrorType.OTHER,
                            resId = R.string.label_network_error
                        )
                    )
                }
            } catch (e: JSONException) {
                //response body cannot be parsed as JSONObject, e.g. when encountering server internal error (500)
                error.postValue(
                    AppErrors(
                        AppErrors.ErrorType.OTHER,
                        resId = R.string.label_network_error
                    )
                )
            }
            return
        }
        error.postValue(AppErrors(AppErrors.ErrorType.OTHER, resId = R.string.label_network_error))
    }

    private fun showProgress() {
        progress.postValue(true)
    }

    private fun hideProgress() {
        progress.postValue(false)
    }

    protected fun <T> callApi(observable: Observable<T>): Observable<T> = observable
        .uiThread()
        .doOnSubscribe { showProgress() }
        .doOnNext { hideProgress() }
        .doOnError { handleError(it) }
        .doOnComplete { hideProgress() }

    protected fun callApi(completable: Completable): Completable = completable
        .uiThread()
        .doOnSubscribe { showProgress() }
        .doOnComplete { hideProgress() }
        .doOnError { handleError(it) }
        .doOnComplete { hideProgress() }

    protected fun <T> callApi(single: Single<T>): Single<T> = single
        .uiThread()
        .doOnSubscribe { showProgress() }
        .doOnSuccess { hideProgress() }
        .doOnError { handleError(it) }

    protected fun <T> justSubscribe(observable: Observable<T>) =
        add(callApi(observable).subscribe({}, { it.printStackTrace() }))

    protected fun justSubscribe(completable: Completable) =
        add(callApi(completable).subscribe({}, { it.printStackTrace() }))

    protected fun <T> justSubscribe(single: Single<T>) =
        add(callApi(single).subscribe({}, { it.printStackTrace() }))

    override fun onCleared() {
        super.onCleared()
        disposeAll()
    }
}