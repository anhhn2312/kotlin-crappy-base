package com.dinominator.kotlin_awesome_app.di.modules

import com.dinominator.kotlin_awesome_app.di.scopes.PerActivity
import com.dinominator.kotlin_awesome_app.ui.modules.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    abstract fun homeActivity(): HomeActivity
}