package org.martellina.rickandmorty.di

import dagger.Component
import org.martellina.rickandmorty.ui.fragments.*
import org.martellina.rickandmorty.ui.viewmodels.ViewModelEpisodes

@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {

//    fun inject(fragmentCharacters: FragmentCharacters)
//    fun inject(fragmentCharacterDetail: FragmentCharacterDetail)
//    fun inject(fragmentEpisodes: FragmentEpisodes)
//    fun inject(fragmentEpisodeDetail: FragmentEpisodeDetail)
//    fun inject(fragmentLocations: FragmentLocations)
//    fun inject(fragmentLocationDetail: FragmentLocationDetail)

    fun inject(viewModelEpisodes: ViewModelEpisodes)

}