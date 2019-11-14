package com.dinominator.kotlin_awesome_app.di.modules

import com.dinominator.data.repository.HomeRepositoryProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideHomeRepository(): HomeRepositoryProvider = HomeRepositoryProvider()

}