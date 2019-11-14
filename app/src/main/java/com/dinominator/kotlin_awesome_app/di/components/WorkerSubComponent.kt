package com.dinominator.kotlin_awesome_app.di.components

import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.dinominator.kotlin_awesome_app.di.modules.WorkersModule
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Provider

@Subcomponent(modules = [WorkersModule::class])
interface WorkerSubComponent {

    fun workers(): Map<Class<out RxWorker>, Provider<RxWorker>>

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun workerParameters(param: WorkerParameters): Builder

        fun build(): WorkerSubComponent
    }
}
