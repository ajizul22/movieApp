package com.reserach.movieapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserTableModel (
    @ColumnInfo(name = "user_name")
    var username: String,

    @ColumnInfo(name = "user_password")
    var password: String,

    @ColumnInfo(name = "user_level")
    var userLevel: Int,

    @ColumnInfo(name = "user_email")
    var userEmail: String,

    @ColumnInfo(name = "user_phone")
    var userPhone: String
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var userId: Int? = null

}