package com.dinominator.kotlin_awesome_app.di.modules

import com.dinominator.kotlin_awesome_app.di.scopes.PerFragment
import com.dinominator.kotlin_awesome_app.ui.modules.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBindingModule {

    @PerFragment
    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): HomeFragment
}