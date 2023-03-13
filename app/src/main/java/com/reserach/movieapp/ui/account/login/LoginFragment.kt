package com.reserach.movieapp.ui.account.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.reserach.movieapp.R
import com.reserach.movieapp.databinding.FragmentLoginBinding
import com.reserach.movieapp.ui.account.register.RegisterFragment

class LoginFragment : Fragment() {
    private lateinit var bind:FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        initComponent()
        return bind.root

    }

    private fun initComponent() {
        bind.apply {
            btnRegister.setOnClickListener {
                val fragmentRegister = RegisterFragment()
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.nav_host_fragment_container_opening, fragmentRegister)
                transaction?.commit()
            }
        }
    }


}