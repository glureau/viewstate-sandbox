package com.glureau.viewstatepattern.view_state.common

import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.InitialValueObservable
import com.jakewharton.rxbinding2.widget.RxTextView

fun TextInputLayout.textChanges(): InitialValueObservable<CharSequence> =
    editText?.let { RxTextView.textChanges(it) }
        ?: InitialValueObservable.error<CharSequence>(IllegalStateException()) as InitialValueObservable<CharSequence>

