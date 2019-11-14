package com.dinominator.kotlin_awesome_app.di.modules

import android.app.Application
import android.content.Context
import com.dinominator.kotlin_awesome_app.di.annotations.ForApplication
import dagger.Module
import dagger.Provides

@Module(includes = [ViewModelModule::class])
class ApplicationModule {

    @Provides
    @ForApplication
    fun providesApplicationContext(application: Application): Context {
        return application
    }
}