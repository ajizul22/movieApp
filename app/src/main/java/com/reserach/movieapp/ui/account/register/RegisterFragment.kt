package com.reserach.movieapp.ui.account.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.reserach.movieapp.R
import com.reserach.movieapp.databinding.FragmentRegisterBinding
import com.reserach.movieapp.ui.account.login.LoginFragment
import com.reserach.movieapp.ui.genre.GenreViewModel
import com.reserach.movieapp.util.GlobalFunc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private lateinit var bind: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var globalFunc: GlobalFunc

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            RegisterViewModel::class.java)
        globalFunc = GlobalFunc()

        initComponent()
//        initData()

        return bind.root
    }



    private fun initComponent() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                loadLoginPage()
            }
        })

        bind.apply {
            btnHaveAccount.setOnClickListener {
                loadLoginPage()
            }

            btnRegister.setOnClickListener {
                doRegister()
            }

            ivBack.setOnClickListener {
                loadLoginPage()
            }
        }
    }
    private fun doRegister() {
        bind.apply {
            val username = etUsername.editText?.text.toString().trim()
            val email = etEmail.editText?.text.toString().trim()
            val phone = etPhone.editText?.text.toString().trim()
            val password = etPassword.editText?.text.toString().trim()
            val passwordValidation = etPasswordValidation.editText?.text.toString().trim()

            if (username.isEmpty()) {
                etUsername.error = "please insert your username"
            } else if (email.isEmpty()) {
                etEmail.error = "please insert your email"
            } else if (phone.isEmpty()) {
                etPhone.error = "please insert your phone number"
            } else if (password.isEmpty()) {
                etPassword.error = "please insert your password"
            } else if (passwordValidation.isEmpty()) {
                etPasswordValidation.error = "please insert your password validation"
            } else if (!password.equals(passwordValidation)) {
                etPasswordValidation.error = "password validation is different"
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.registerUser(requireContext(), username, password, phone, email)
                    Toast.makeText(requireContext(), "Success Register Account", Toast.LENGTH_SHORT).show()
                    loadLoginPage()
                }
            }
        }
    }

//    private fun initData() {
//        viewModel.isSuccessRegister.observe(viewLifecycleOwner) {
//            if (it) {
//                Toast.makeText(requireContext(), "Success Register Account", Toast.LENGTH_SHORT).show()
//                loadLoginPage()
//            } else {
//                globalFunc.showDialogError(requireContext(), "fail to register account")
//            }
//        }
//    }

    private fun loadLoginPage() {
        val fragmentLogin = LoginFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.nav_host_fragment_container_opening, fragmentLogin)
        transaction?.commit()
    }
}