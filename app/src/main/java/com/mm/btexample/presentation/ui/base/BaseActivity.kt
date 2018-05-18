package com.mm.btexample.presentation.ui.base

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import com.mm.btexample.R

/**
 * Created by Maksim Makeychik on 18.05.2018.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected fun setToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

}
