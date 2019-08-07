package com.glureau.poc.register

import androidx.lifecycle.ViewModel
import com.glureau.poc.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class RegisterModule {
    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    internal abstract fun registerViewModel(registerViewModel: RegisterViewModel): ViewModel
}