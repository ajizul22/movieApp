package com.reserach.movieapp.ui.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.reserach.movieapp.R
import com.reserach.movieapp.databinding.ActivityOpeningBinding
import com.reserach.movieapp.ui.account.login.LoginFragment

class OpeningActivity : AppCompatActivity() {
    private lateinit var bind: ActivityOpeningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_opening)

        initComponent()

    }

    private fun initComponent() {
        bind.apply {
            val loginFragment = LoginFragment()
            loadFragment(loginFragment)
        }
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setMessage("Exit apps?")
            .setNegativeButton("cancel", null)
            .setPositiveButton("yes") { dialog, which ->
                // Respond to positive button press
                super.onBackPressed()
            }
            .show()
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_container_opening, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}