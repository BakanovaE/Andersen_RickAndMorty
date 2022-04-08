package org.martellina.rickandmorty.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.martellina.rickandmorty.data.Repository
import org.martellina.rickandmorty.data.RepositoryImpl
import org.martellina.rickandmorty.network.model.LocationInfo
import org.martellina.rickandmorty.network.model.LocationsFilter
import javax.inject.Inject

class ViewModelLocations: ViewModel() {

    var locationsList = MutableLiveData<List<LocationInfo>>()
    var isLoading = MutableLiveData<Boolean>()
    var pages = MutableLiveData<Int>()
    var isEmpty = MutableLiveData<Boolean>()

    val repository = RepositoryImpl.get()

    fun getAllLocations(page: Int, filter: LocationsFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAllLocations(page, filter)
            launch(Dispatchers.Main) {
                updateLiveData(result?.results, filter)
                updatePages(result?.info?.pages)
            }
        }
    }

    fun getFilteredLocations(filter: LocationsFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getFilteredLocations(filter)
                    launch(Dispatchers.Main) {
                        setFilteredList(result?.results)
                        if (result != null) {
                            if(result.results.isEmpty()) {
                                isEmpty.value = true
                            }
                    }
                        updatePages(result?.info?.pages)
            }
        }
    }

    private fun updateLiveData(list: List<LocationInfo>?, filter: LocationsFilter) {
        if (filter.name.isNullOrEmpty() || filter.type.isNullOrEmpty() || filter.dimension.isNullOrEmpty()) {
            if (locationsList.value.isNullOrEmpty()) {
                locationsList.value = list
            } else {
                if (list != null) {
                    for (location in list) {
                        locationsList.value = locationsList.value?.plus(location)
                    }
                }
            }
        } else {
            locationsList.value = list
        }
        isLoading.value = false
    }

    private fun setFilteredList(list: List<LocationInfo>?) {
        locationsList.value = list
        isLoading.value = false
    }


    private fun updatePages(pages: Int?) {
        this.pages.value = pages
    }

}