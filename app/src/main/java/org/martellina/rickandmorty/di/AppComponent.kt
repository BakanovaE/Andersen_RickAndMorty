package org.martellina.rickandmorty.di

import dagger.Component
import org.martellina.rickandmorty.data.RepositoryImpl
import org.martellina.rickandmorty.ui.fragments.*
import org.martellina.rickandmorty.ui.viewmodels.*
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RepositoryModule::class, RetrofitModule::class, DataModule::class])
interface AppComponent {

    fun inject(fragmentEpisodes: FragmentEpisodes)
    fun inject(fragmentLocations: FragmentLocations)
    fun inject(fragmentCharacters: FragmentCharacters)
    fun inject(fragmentEpisodeDetail: FragmentEpisodeDetail)
    fun inject(fragmentLocationDetail: FragmentLocationDetail)
    fun inject(fragmentCharacterDetail: FragmentCharacterDetail)

    fun inject(viewModelCharacters: ViewModelCharacters)
    fun inject(viewModelEpisodes: ViewModelEpisodes)
    fun inject(viewModelLocations: ViewModelLocations)
    fun inject(viewModelEpisode: ViewModelEpisode)
    fun inject(viewModelLocation: ViewModelLocation)
    fun inject(viewModelCharacter: ViewModelCharacter)

    fun inject(repositoryImpl: RepositoryImpl)

}