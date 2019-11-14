package com.dinominator.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dinominator.data.persistence.models.UserModel

/**
 * Created by Andy Ha on 3/20/2019.
 */

@Dao
abstract class UserDao : BaseDao<UserModel>() {
    @Query("SELECT * FROM ${UserModel.TABLE_NAME} LIMIT 1")
    abstract fun getUser(): UserModel?

    @Query("DELETE FROM ${UserModel.TABLE_NAME}")
    abstract fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(vararg userModel: UserModel)
}