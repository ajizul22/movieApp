package com.reserach.movieapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.reserach.movieapp.data.local.model.UserTableModel

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun InsertData(userTableModel: UserTableModel)

    @Query("SELECT * FROM User WHERE user_email =:email")
    fun getUserDetails(email: String?) : LiveData<UserTableModel>

    @Update
    fun updateUser(userTableModel: UserTableModel)

}