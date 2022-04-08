package org.martellina.rickandmorty.di

import dagger.Component
import dagger.Provides
import org.martellina.rickandmorty.ui.fragments.*
import org.martellina.rickandmorty.ui.viewmodels.ViewModelCharacters
import org.martellina.rickandmorty.ui.viewmodels.ViewModelEpisodes
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RepositoryModule::class])
interface AppComponent {

    fun inject(fragmentEpisodes: FragmentEpisodes)
    fun inject(fragmentLocations: FragmentLocations)
    fun inject(fragmentCharacters: FragmentCharacters)
    fun inject(fragmentEpisodeDetail: FragmentEpisodeDetail)
    fun inject(fragmentLocationDetail: FragmentLocationDetail)
    fun inject(fragmentCharacterDetail: FragmentCharacterDetail)
    fun inject(viewModelCharacters: ViewModelCharacters)

}