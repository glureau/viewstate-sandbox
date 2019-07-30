package com.glureau.viewstatepattern.view_state

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.InitialValueObservable
import com.jakewharton.rxbinding2.widget.RxTextView

// TODO: Need to manage diffing better than that, this is just for demo (there is libs available)

fun TextInputLayout.setTextIfDifferent(txt: String) {
    if (editText?.text.toString() != txt) {
        editText?.setText(txt)
    }
}

fun TextInputLayout.textChanges(): InitialValueObservable<CharSequence> =
    editText?.let { RxTextView.textChanges(it) } ?: InitialValueObservable.error<CharSequence>(IllegalStateException()) as InitialValueObservable<CharSequence>

fun EditText.setTextIfDifferent(txt: String) {
    if (text.toString() != txt) {
        setText(txt)
    }
}