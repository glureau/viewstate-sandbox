package com.glureau.poc.di

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.glureau.poc.register.RegisterFragment

interface AppInjectorProvider {
    fun appInjector(): AppInjector
}

fun Activity.appInjector() = (this.application as AppInjectorProvider).appInjector()
fun Fragment.appInjector() = (this.activity?.application as AppInjectorProvider?)?.appInjector()
    ?: kotlin.error("Cannot inject without a proper reference to the application")

fun View.appInjector() = (this.context.applicationContext as AppInjectorProvider).appInjector()

interface AppInjector {
    fun inject(registerFragment: RegisterFragment)
}