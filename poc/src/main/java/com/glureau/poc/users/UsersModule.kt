package com.glureau.poc.users

import androidx.lifecycle.ViewModel
import com.glureau.poc.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class UsersModule {
    @Binds
    @IntoMap
    @ViewModelKey(UsersViewModel::class)
    internal abstract fun usersViewModel(usersViewModel: UsersViewModel): ViewModel
}