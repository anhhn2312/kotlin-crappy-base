package com.dinominator.kotlin_awesome_app.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dinominator.kotlin_awesome_app.di.annotations.ViewModelKey
import com.dinominator.kotlin_awesome_app.platform.viewmodel.ViewModelFactory
import com.dinominator.kotlin_awesome_app.ui.modules.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
}