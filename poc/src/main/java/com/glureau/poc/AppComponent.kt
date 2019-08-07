package com.glureau.poc

import com.glureau.poc.di.AppInjector
import com.glureau.poc.register.RegisterModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [RegisterModule::class])
@Singleton
interface AppComponent : AppInjector