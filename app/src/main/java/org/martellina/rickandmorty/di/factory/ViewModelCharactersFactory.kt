package org.martellina.rickandmorty.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.martellina.rickandmorty.data.RepositoryImpl
import org.martellina.rickandmorty.ui.viewmodels.ViewModelCharacters
import javax.inject.Inject

class ViewModelCharactersFactory @Inject constructor(private val repository: RepositoryImpl): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelCharacters(repository) as T
    }
}
