package org.martellina.rickandmorty.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.martellina.rickandmorty.data.Repository
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.CharactersFilter
import org.martellina.rickandmorty.network.model.EpisodesFilter
import javax.inject.Inject

class ViewModelCharacters @Inject constructor(private val repository: Repository): ViewModel() {

    var charactersList = MutableLiveData<List<CharacterInfo>>()
    var isLoading = MutableLiveData<Boolean>()
    var pages = MutableLiveData<Int>()
    var isEmptyFilteredResult = MutableLiveData<Boolean>()
    var isEmpty = MutableLiveData<Boolean>()

    fun getAllCharacters(page: Int, filter: CharactersFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAllCharacters(page, filter)
            launch(Dispatchers.Main) {
                updateLiveData(result?.results)
                updatePages(result?.info?.pages)
            }
        }
    }

    fun getFilteredCharacters(filter: CharactersFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getFilteredCharacters(filter)
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

    fun reloadCharacters(page: Int, filter: CharactersFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAllCharacters(page, filter)
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

    private fun updateLiveData(list: List<CharacterInfo>?) {
            if (charactersList.value.isNullOrEmpty()) {
                if (list.isNullOrEmpty()) {
                    isEmpty.value = true
                } else {
                    charactersList.value = list
                }
            } else {
                if (list != null) {
                    for (character in list) {
                        charactersList.value = charactersList.value?.plus(character)
                    }
                }
            }
        isEmpty.value = false
        isLoading.value = false
    }

    private fun setFilteredList(list: List<CharacterInfo>?) {
        charactersList.value = list
        isLoading.value = false
    }

    private fun updatePages(pages: Int?) {
        this.pages.value = pages
    }

}