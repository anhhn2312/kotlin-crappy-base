package com.dinominator.kotlin_awesome_app.di.modules

import dagger.Module

@Module(includes = [AppDatabaseModule::class, NetworkModule::class])
class UseCaseModule {
//    @PerFragment
//    @Provides
//    fun getUserUseCase(provider: UserRepositoryProvider) = GetUserUseCase(provider)
}