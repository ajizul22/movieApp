package com.reserach.movieapp.util

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.reserach.movieapp.R

class GlobalFunc {
    fun responseError(code: Int): String {
        return when (code) {
            401 -> "Invalid API key: You must be granted a valid key."
            404 -> "The resource you requested could not be found."
            500 -> "Internal server error"
            else -> "something error"
        }
    }

    fun showDialogError(context: Context, message: String) {
        MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
            .setTitle("Something Wrong..")
            .setMessage(message)
            .setPositiveButton("Ok", null)
            .show()
    }

}