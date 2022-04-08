package org.martellina.rickandmorty.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import org.martellina.rickandmorty.data.Repository
import org.martellina.rickandmorty.data.RepositoryImpl
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideRepository(repository: RepositoryImpl): Repository

}