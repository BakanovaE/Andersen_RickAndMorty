package org.martellina.rickandmorty.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.martellina.rickandmorty.ui.viewmodels.ViewModelEpisodes
import org.martellina.rickandmorty.ui.viewmodels.ViewModelLocation
import javax.inject.Inject

class ViewModelLocationFactory @Inject constructor(): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelLocation() as T
    }
}