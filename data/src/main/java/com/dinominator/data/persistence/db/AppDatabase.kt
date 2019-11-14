package com.dinominator.data.persistence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dinominator.data.persistence.dao.UserDao
import com.dinominator.data.persistence.models.UserModel
import com.dinominator.resources.DbConfig

const val VERSION = 1
const val DATABASE_NAME = DbConfig.DATABASE_NAME

@Database(
    version = VERSION,
    entities = [
        UserModel::class
    ],
    exportSchema = false
)
@TypeConverters(
    DateConverter::class,
    StringArrayConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    //define DAO get method here...
    abstract fun getUserDao(): UserDao

    fun clearAll() = clearAllTables()
}