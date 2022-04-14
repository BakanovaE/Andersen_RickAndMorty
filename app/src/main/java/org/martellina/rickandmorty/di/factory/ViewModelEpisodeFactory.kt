package org.martellina.rickandmorty.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.martellina.rickandmorty.data.RepositoryImpl
import org.martellina.rickandmorty.ui.viewmodels.ViewModelEpisode
import javax.inject.Inject

class ViewModelEpisodeFactory @Inject constructor(private val repository: RepositoryImpl): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelEpisode(repository) as T
    }
}