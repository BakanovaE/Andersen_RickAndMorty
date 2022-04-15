package org.martellina.rickandmorty.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.martellina.rickandmorty.data.Repository
import org.martellina.rickandmorty.network.model.LocationInfo
import org.martellina.rickandmorty.network.model.LocationsFilter
import javax.inject.Inject

class ViewModelLocations@Inject constructor(private val repository: Repository): ViewModel() {

    var locationsList = MutableLiveData<List<LocationInfo>?>()
    var isLoading = MutableLiveData<Boolean>()
    var pages = MutableLiveData<Int>()
    var isEmptyFilteredResult = MutableLiveData<Boolean>()
    var isEmpty = MutableLiveData<Boolean>()

    fun getAllLocations(page: Int, filter: LocationsFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAllLocations(page, filter)
            launch(Dispatchers.Main) {
                updateLiveData(result?.results)
                updatePages(result?.info?.pages)
            }
        }
    }

    fun getFilteredLocations(filter: LocationsFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getFilteredLocations(filter)
                    launch(Dispatchers.Main) {
                        result?.let {
                        setFilteredList(it.results)
                            if(it.results.isEmpty()) {
                                isEmptyFilteredResult.value = true
                            }
                            isEmptyFilteredResult.value = false
                    }
                        updatePages(result?.info?.pages)
            }
        }
    }

    fun reloadLocations(page: Int, filter: LocationsFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAllLocations(page, filter)
            launch(Dispatchers.Main) {
                result?.let {
                    setFilteredList(it.results)
                    if (it.results.isEmpty()) {
                        isEmptyFilteredResult.value = true
                    }
                    isEmptyFilteredResult.value = false
                }
                updatePages(result?.info?.pages)
            }
        }
    }

    private fun updateLiveData(list: List<LocationInfo>?) {
            if (locationsList.value.isNullOrEmpty()) {
                if(list.isNullOrEmpty()) {
                    isEmpty.value = true
                } else {
                    locationsList.value = list
                }
            } else {
                if (list != null) {
                    for (location in list) {
                        locationsList.value = locationsList.value?.plus(location)
                    }
                }
            }
        isLoading.value = false
        isEmpty.value = false
    }

    private fun setFilteredList(list: List<LocationInfo>?) {
        locationsList.value = list
        isLoading.value = false
    }

    private fun updatePages(pages: Int?) {
        this.pages.value = pages
    }
}