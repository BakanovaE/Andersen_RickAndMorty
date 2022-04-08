package org.martellina.rickandmorty.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule constructor(private val context: Context){

    @Provides
    fun provideContext() : Context{ return context }

}