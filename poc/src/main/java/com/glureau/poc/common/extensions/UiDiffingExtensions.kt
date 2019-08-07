package com.glureau.poc.common.extensions

import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout


// TODO: Manage diffing with CharSequence to diff Spans
fun TextInputLayout.setTextIfDifferent(txt: String?) {
    editText?.setTextIfDifferent(txt)
}

fun TextView.setTextIfDifferent(txt: String?) {
    if (text.toString() != txt) {
        text = txt
    }
}

fun TextInputLayout.setErrorIfDifferent(txt: String?) {
    if (error != txt) {
        error = txt
    }
}
fun TextView.setErrorIfDifferent(txt: String?) {
    if (error != txt) {
        error = txt
    }
}
