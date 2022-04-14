package org.martellina.rickandmorty.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.martellina.rickandmorty.data.RepositoryImpl
import org.martellina.rickandmorty.ui.viewmodels.ViewModelCharacter
import javax.inject.Inject

class ViewModelCharacterFactory @Inject constructor(private val repository: RepositoryImpl): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelCharacter(repository) as T
    }
}