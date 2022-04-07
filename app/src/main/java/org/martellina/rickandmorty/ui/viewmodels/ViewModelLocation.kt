package org.martellina.rickandmorty.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.martellina.rickandmorty.network.Repository
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.LocationInfo

class ViewModelLocation: ViewModel() {

    var location = MutableLiveData<LocationInfo>()
    var charactersListLiveData = MutableLiveData<List<CharacterInfo>>()
    var isLoading = MutableLiveData<Boolean>()

    private val repository = Repository.get()

    fun getLocationById(id: Int) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getLocationById(id)
            launch(Dispatchers.Main) {
                location.postValue(result)
                updateLiveData(result)
            }
        }
    }

    fun getCharactersById(charactersList: List<String>) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = ArrayList<CharacterInfo>()
            for (id in charactersList) {
                val singleId = id.split("/").last().toInt()
                val character = repository.getCharacterById(singleId)
                character?.let { result.add(it) }
            }
            launch(Dispatchers.Main) {
                updateCharactersListLiveData(result)
            }
        }
    }

    private fun updateCharactersListLiveData(charactersList: List<CharacterInfo>?) {
        this.charactersListLiveData.value = charactersList
        isLoading.value = false
    }

    private fun updateLiveData(episode: LocationInfo?) {
        this.location.value = episode
        isLoading.value = false
    }

}