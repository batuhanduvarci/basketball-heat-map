package com.example.basketballheatmap.presentation.extensions

import androidx.fragment.app.FragmentManager
import com.example.basketballheatmap.presentation.dialogs.ProgressDialogFragment

fun showLoading(fragmentManager : FragmentManager) {
    val progressDialogFragment = ProgressDialogFragment().newInstance()

    fragmentManager.beginTransaction()
        .addToBackStack(null)

    progressDialogFragment.show(fragmentManager, "progressDialog")
}

fun hideLoading(fragmentManager: FragmentManager){
    val progressDialogFragment = fragmentManager.findFragmentByTag("progressDialog") as ProgressDialogFragment?
    progressDialogFragment?.dismiss()
}