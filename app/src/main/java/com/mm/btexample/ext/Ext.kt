package com.mm.btexample.ext

import android.content.Context
import android.widget.Toast

/**
 * Created by Max Makeichik on 18-May-18.
 */
fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}