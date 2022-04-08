package org.martellina.rickandmorty.ui.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.martellina.rickandmorty.data.Repository
import org.martellina.rickandmorty.data.RepositoryImpl
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.CharactersFilter
import javax.inject.Inject

class ViewModelCharacters @Inject constructor(private val repository: Repository): ViewModel() {

    var charactersList = MutableLiveData<List<CharacterInfo>>()
    var isLoading = MutableLiveData<Boolean>()
    var pages = MutableLiveData<Int>()
    var isEmpty = MutableLiveData<Boolean>()

    fun getAllCharacters(page: Int, filter: CharactersFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAllCharacters(page, filter)
            launch(Dispatchers.Main) {
                updateLiveData(result?.results, filter)
                updatePages(result?.info?.pages)
            }
        }
    }

    fun getFilteredCharacters(filter: CharactersFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getFilteredCharacters(filter)
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

    private fun updateLiveData(list: List<CharacterInfo>?, filter: CharactersFilter) {
        if (filter.name.isNullOrEmpty() || filter.status.isNullOrEmpty() || filter.species.isNullOrEmpty() || filter.type.isNullOrEmpty() || filter.gender.isNullOrEmpty()) {
            if (charactersList.value.isNullOrEmpty()) {
                charactersList.value = list
            } else {
                if (list != null) {
                    for (character in list) {
                        charactersList.value = charactersList.value?.plus(character)
                    }
                }
            }
        } else {
            charactersList.value = list
        }
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