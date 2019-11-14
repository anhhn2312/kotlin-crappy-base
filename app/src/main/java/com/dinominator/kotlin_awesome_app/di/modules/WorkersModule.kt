package com.dinominator.kotlin_awesome_app.di.modules

import androidx.work.RxWorker
import com.dinominator.kotlin_awesome_app.di.annotations.WorkerKey
import com.dinominator.kotlin_awesome_app.platform.works.DummyWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WorkersModule {
    @Binds
    @IntoMap
    @WorkerKey(DummyWorker::class)
    abstract fun bindDummyWorker(worker: DummyWorker): RxWorker
}