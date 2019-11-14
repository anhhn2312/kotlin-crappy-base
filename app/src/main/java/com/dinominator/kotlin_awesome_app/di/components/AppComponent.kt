package com.dinominator.kotlin_awesome_app.di.components

import android.app.Application
import com.dinominator.data.persistence.db.AppDatabase
import com.dinominator.kotlin_awesome_app.KotlinAwesomeApp
import com.dinominator.kotlin_awesome_app.di.modules.*
import com.dinominator.kotlin_awesome_app.platform.workmanager.DaggerWorkerFactory
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AppDatabaseModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        ActivityBindingModule::class,
        ActivityModule::class,
        AndroidSupportInjectionModule::class
//        FirebaseServiceModule::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun appDatabaseModule(databaseModule: AppDatabaseModule): Builder

        @BindsInstance
        fun networkModule(networkModule: NetworkModule): Builder

        @BindsInstance
        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun repositoryModule(repositoryModule: RepositoryModule): Builder

        fun build(): AppComponent
    }

    fun daggerWorkerFactory(): DaggerWorkerFactory

    fun workerSubComponentBuilder(): WorkerSubComponent.Builder

    fun appDatabase(): AppDatabase

    fun inject(app: KotlinAwesomeApp)

    override fun inject(instance: DaggerApplication)

    companion object {
        fun getComponent(app: KotlinAwesomeApp): AppComponent = DaggerAppComponent.builder()
            .application(app)
            .appDatabaseModule(AppDatabaseModule())
            .networkModule(NetworkModule())
            .activityModule(ActivityModule())
            .repositoryModule(RepositoryModule())
            .build()
    }
}
