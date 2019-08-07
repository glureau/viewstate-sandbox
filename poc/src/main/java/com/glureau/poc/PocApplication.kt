package com.glureau.poc

import android.app.Application
import com.glureau.poc.di.ViewModelFactoryComponent

class PocApplication : Application(), ViewModelFactoryComponent {

    private lateinit var appComponent: AppComponent
    override fun getDaggerJetpackViewModelFactory() = appComponent.getDaggerJetpackViewModelFactory()

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }
}