package com.dinominator.data.persistence.models

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dinominator.data.utils.ImageUtils

@Entity(tableName = UserModel.TABLE_NAME)
data class UserModel(
    @ColumnInfo(name = "id") @PrimaryKey var id: Long = 1,
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "phoneNumber") var phoneNumber: String? = null,
    @ColumnInfo(name = "address") var address: String? = null,
    @ColumnInfo(name = "avatar") var avatar: String? = null
) {
    companion object {
        const val TABLE_NAME = "user"
    }
}

@BindingAdapter("avatar")
fun bindAvatar(view: ImageView, avatarUrl: String?) {
    if (!avatarUrl.isNullOrBlank()) {
        ImageUtils.loadImage(view, avatarUrl)
    }
}


