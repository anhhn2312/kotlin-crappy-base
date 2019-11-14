package com.dinominator.kotlin_awesome_app.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.dinominator.data.persistence.db.DATABASE_NAME
import com.dinominator.data.persistence.db.AppDatabase
import com.dinominator.data.storage.AppSharedPreference
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.dinominator.kotlin_awesome_app.di.annotations.ForApplication
import com.dinominator.kotlin_awesome_app.di.annotations.ForDB
import dagger.Module
import dagger.Provides
import java.lang.reflect.Modifier
import javax.inject.Singleton

@Module
class AppDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ForApplication context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .allowMainThreadQueries() // allow querying on MainThread (this useful in some cases)
            .fallbackToDestructiveMigration() //  this mean that it will delete all tables and recreate them after version is changed
            .build()

    @Singleton
    @Provides
    fun providePreference(app: Application, gson: Gson): AppSharedPreference = AppSharedPreference(app, gson)

    @ForDB
    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .create()
}