package com.glureau.poc.common.extensions

import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.setTextIfDifferent(txt: CharSequence?) {
    editText?.setTextIfDifferent(txt)
}

fun TextView.setTextIfDifferent(txt: CharSequence?) {
    if (text != txt) {
        text = txt
    }
}

fun TextInputLayout.setErrorIfDifferent(txt: CharSequence?) {
    editText?.setErrorIfDifferent(txt)
}
fun TextView.setErrorIfDifferent(txt: CharSequence?) {
    if (error != txt) {
        error = txt
    }
}
