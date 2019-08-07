package com.glureau.poc

import android.app.Application
import com.glureau.poc.di.AppInjectorProvider

class PocApplication : Application(), AppInjectorProvider {
    override fun appInjector() = appComponent
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .build()
    }
}