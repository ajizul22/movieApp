package com.reserach.movieapp.ui.account.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.reserach.movieapp.R
import com.reserach.movieapp.databinding.FragmentRegisterBinding
import com.reserach.movieapp.ui.account.login.LoginFragment

class RegisterFragment : Fragment() {
    private lateinit var bind: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        initComponent()

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
        }
    }

    private fun loadLoginPage() {
        val fragmentLogin = LoginFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.nav_host_fragment_container_opening, fragmentLogin)
        transaction?.commit()
    }
}