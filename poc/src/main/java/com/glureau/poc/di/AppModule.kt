package com.glureau.poc.di

import android.content.Context
import com.glureau.poc.common.pattern.ColorResolver
import com.glureau.poc.common.pattern.DimensionResolver
import com.glureau.poc.common.pattern.ResolverImpl
import com.glureau.poc.common.pattern.StringResolver
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val appContext: Context) {

    @Provides
    fun provideStringResolver(): StringResolver = ResolverImpl(appContext)

    @Provides
    fun provideColorResolver(): ColorResolver = ResolverImpl(appContext)

    @Provides
    fun provideDimensionResolver(): DimensionResolver = ResolverImpl(appContext)


}