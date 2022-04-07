package org.martellina.rickandmorty.di

import android.content.Context
import dagger.Module
import dagger.Provides
import org.martellina.rickandmorty.network.Repository
import javax.inject.Singleton

@Module
class VMModule {

    @Singleton
    @Provides
    fun provideRepository(context: Context) : Repository {
        return Repository.get()
    }
}