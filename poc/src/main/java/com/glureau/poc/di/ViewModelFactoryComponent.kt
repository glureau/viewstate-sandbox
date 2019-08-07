package com.glureau.poc.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get

/**
 * Make your app component extends this interface to use the viewModel functionnality.
 */
interface ViewModelFactoryComponent {
    fun getDaggerJetpackViewModelFactory(): DaggerJetpackViewModelFactory
}

inline fun <reified T : ViewModel> FragmentActivity.viewModel(): Lazy<T> = lazy {
    ViewModelProviders.of(
        this,
        (this.application as ViewModelFactoryComponent).getDaggerJetpackViewModelFactory()
    ).get<T>()
}

inline fun <reified T : ViewModel> Fragment.viewModel(): Lazy<T> = lazy {
    ViewModelProviders.of(
        this,
        (this.activity?.application as ViewModelFactoryComponent).getDaggerJetpackViewModelFactory()
    ).get<T>()
}