package com.glureau.poc

import com.glureau.poc.di.ViewModelFactoryComponent
import com.glureau.poc.register.RegisterModule
import com.glureau.poc.users.UsersModule
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        RegisterModule::class,
        UsersModule::class
    ]
)
@Singleton
interface AppComponent : ViewModelFactoryComponent