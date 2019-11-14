package com.dinominator.kotlin_awesome_app.ui.modules.home

import androidx.lifecycle.MutableLiveData
import com.dinominator.data.persistence.models.UserModel
import com.dinominator.data.repository.HomeRepositoryProvider
import com.dinominator.kotlin_awesome_app.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by Andy Ha on 6/21/2019.
 */

class HomeViewModel @Inject constructor(
    var repository: HomeRepositoryProvider
) : BaseViewModel() {

    val userLiveData = MutableLiveData<UserModel>()

    internal fun getUser() {
        justSubscribe(repository.getUser()
            .doOnSuccess {
                userLiveData.postValue(it)
            }
        )
    }
}