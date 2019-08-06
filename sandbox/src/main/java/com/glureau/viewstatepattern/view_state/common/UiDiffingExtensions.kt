package com.glureau.viewstatepattern.view_state.common

import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.setTextIfDifferent(txt: CharSequence) {
    editText?.setTextIfDifferent(txt)
}

fun TextView.setTextIfDifferent(txt: CharSequence) {
    if (text.toString() != txt) {
        text = txt
    }
}
