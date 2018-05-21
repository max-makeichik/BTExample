package com.mm.btexample.presentation.ui.activity.base

import android.support.v7.widget.Toolbar
import com.arellomobile.mvp.MvpAppCompatActivity
import com.mm.btexample.R
import com.mm.btexample.ext.toast

/**
 * Created by Maksim Makeychik on 18.05.2018.
 */
abstract class BaseActivity : MvpAppCompatActivity() {

    protected fun setToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    fun showLoading() {
    }

    fun hideLoading() {
    }

    fun showError(errorMessage: String) {
        toast(errorMessage)
    }

    fun hideError() {}

}
