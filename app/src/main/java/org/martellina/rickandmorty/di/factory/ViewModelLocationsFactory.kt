package org.martellina.rickandmorty.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.martellina.rickandmorty.ui.viewmodels.ViewModelEpisodes
import org.martellina.rickandmorty.ui.viewmodels.ViewModelLocations
import javax.inject.Inject

class ViewModelLocationsFactory @Inject constructor(): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelLocations() as T
    }
}