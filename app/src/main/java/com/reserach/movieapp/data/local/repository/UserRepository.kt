package com.reserach.movieapp.data.local.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.reserach.movieapp.data.local.MovieDatabase
import com.reserach.movieapp.data.local.model.UserTableModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class UserRepository {

    companion object {

        var userDatabase: MovieDatabase? = null

        var userTableModel: LiveData<UserTableModel>? = null

        fun initializeDB(context: Context) : MovieDatabase {
            return MovieDatabase.getDatabaseClient(context)
        }

        fun insertData(context: Context, username: String, password: String, phoneNumber: String, email: String, userLevel: Int) {

            userDatabase = initializeDB(context)

            // catch response insert: if null = failure insert

            CoroutineScope(IO).launch {
                val userDetails = UserTableModel(username, password, userLevel, email, phoneNumber)
                userDatabase!!.userDao().InsertData(userDetails)
            }

        }

        fun getUserDetail(context: Context, username: String) : LiveData<UserTableModel>? {

            userDatabase = initializeDB(context)

            userTableModel = userDatabase!!.userDao().getUserDetails(username)

            return userTableModel

        }

    }

}