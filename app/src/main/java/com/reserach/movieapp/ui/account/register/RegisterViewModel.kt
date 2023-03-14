package com.reserach.movieapp.ui.account.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reserach.movieapp.data.local.repository.UserRepository

class RegisterViewModel: ViewModel() {

    val isSuccessRegister = MutableLiveData<Boolean>()

    fun registerUser(context: Context, username: String, password:String, phone: String, email: String) {
        UserRepository.insertData(context, username, password, phone, email, 0)
    }
}