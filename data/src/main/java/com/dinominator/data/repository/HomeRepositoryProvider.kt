package com.dinominator.data.repository

/**
 * Created by Andy Ha on 5/4/2019.
 */

import com.dinominator.data.persistence.models.UserModel
import io.reactivex.Single

class HomeRepositoryProvider : HomeRepository {
    override fun getUser(): Single<UserModel> {
        val user = UserModel(
            email = "dev@dinominator.io",
            name = "dinominator",
            phoneNumber = "011888888",
            address = "Vietnam",
            avatar = "https://avatars2.githubusercontent.com/u/5152914?s=460&v=4"
        )
        return Single.just(user)
    }
}

internal interface HomeRepository {
    fun getUser(): Single<UserModel>
}