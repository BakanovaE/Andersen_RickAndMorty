package org.martellina.rickandmorty.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.martellina.rickandmorty.ui.viewmodels.ViewModelCharacter
import org.martellina.rickandmorty.ui.viewmodels.ViewModelEpisodes
import javax.inject.Inject

class ViewModelCharacterFactory @Inject constructor(): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelCharacter() as T
    }
}