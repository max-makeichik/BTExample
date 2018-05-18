package com.mm.btexample.presentation.ui

import android.os.Bundle
import com.mm.btexample.R
import com.mm.btexample.presentation.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar()
    }
}
