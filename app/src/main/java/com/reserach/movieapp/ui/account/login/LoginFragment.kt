package com.reserach.movieapp.ui.account.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.reserach.movieapp.R
import com.reserach.movieapp.databinding.FragmentLoginBinding
import com.reserach.movieapp.ui.MainActivity
import com.reserach.movieapp.ui.account.register.RegisterFragment
import com.reserach.movieapp.ui.account.register.RegisterViewModel
import com.reserach.movieapp.util.GlobalFunc

class LoginFragment : Fragment() {
    private lateinit var bind:FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var globalFunc: GlobalFunc

    var inputEmail: String = ""
    var inputPassword: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            LoginViewModel::class.java)
        globalFunc = GlobalFunc()

        initComponent()

        return bind.root

    }

    private fun initComponent() {
        bind.apply {
            btnRegister.setOnClickListener {
                loadRegisterPage()
            }

            btnLogin.setOnClickListener {
                validation()
            }
        }
    }

    private fun initData() {
        viewModel.getUserDetails(requireContext(), inputEmail)?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val password = it.password

                if (password != inputPassword) {
                    bind.etPassword.error = "wrong password"
                } else {
                    activity?.let {it1 ->
                        val intent = Intent(it1, MainActivity::class.java)
                        it1.startActivity(intent)
                    }
                }
            } else {
                bind.etPassword.error = null
                bind.tvErrorLogin.setText("email or password invalid")
            }
        })
    }

    private fun validation() {
        bind.apply {
            inputEmail = etEmail.editText?.text.toString().trim()
            inputPassword = etPassword.editText?.text.toString().trim()

            if (inputEmail.isEmpty()) {
                etEmail.error = "please insert your email"
            } else if (inputPassword.isEmpty()) {
                etPassword.error = "please insert your password"
            } else {
                initData()
//                viewModel.getUserDetails(requireContext(), inputEmail)
            }
        }
    }

    private fun loadRegisterPage() {
        val fragment = RegisterFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.nav_host_fragment_container_opening, fragment)
        transaction?.addToBackStack(fragment::class.java.name)
        transaction?.commit()
    }

}