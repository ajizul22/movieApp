package com.reserach.movieapp.ui.account.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.reserach.movieapp.data.local.model.UserTableModel
import com.reserach.movieapp.data.local.repository.UserRepository

class LoginViewModel : ViewModel() {
    // local
    var dataUser: LiveData<UserTableModel>? = null

    fun getUserDetails(context: Context, email: String): LiveData<UserTableModel>? {
        dataUser = UserRepository.getUserDetail(context, email)

        return dataUser
    }
}