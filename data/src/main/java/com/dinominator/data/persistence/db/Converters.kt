package com.dinominator.data.persistence.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.dinominator.extensions.toJson
import java.util.*

class DateConverter {
    @TypeConverter
    fun toDbValue(date: Date? = null): Long? = date?.time

    @TypeConverter
    fun fromDbToValue(date: Long? = 0): Date? = date?.let { Date(it) }
}

class StringArrayConverter {
    @TypeConverter
    fun toDbValue(data: List<String>? = null): String? = data?.toJson()

    @TypeConverter
    fun fromDbToValue(data: String? = null): List<String>? = data?.let {
        Gson().fromJson(it, object : TypeToken<List<String>>() {}.type)
    }
}