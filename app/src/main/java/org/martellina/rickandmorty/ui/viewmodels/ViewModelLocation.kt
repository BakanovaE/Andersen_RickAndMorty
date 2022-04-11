package org.martellina.rickandmorty.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.martellina.rickandmorty.data.Repository
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.LocationInfo
import javax.inject.Inject

class ViewModelLocation @Inject constructor(private val repository: Repository): ViewModel() {

    var location = MutableLiveData<LocationInfo>()
    var charactersListLiveData = MutableLiveData<List<CharacterInfo>>()
    var isLoading = MutableLiveData<Boolean>()
    var isNoCharacters = MutableLiveData<Boolean>()
    var isNoCharactersFound = MutableLiveData<Boolean>()
    var isNotEnoughCharactersFound = MutableLiveData<Boolean>()

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

    fun getCharactersById(charactersUrlList: List<String>) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = ArrayList<CharacterInfo>()
            for (characterUrl in charactersUrlList) {
                if (characterUrl != "") {
                    val singleId = characterUrl.split("/").last().toInt()
                    val character = repository.getCharacterById(singleId)
                    character?.let { result.add(it) }
                }
            }
            launch(Dispatchers.Main) {
                updateCharactersListLiveData(result, charactersUrlList)
            }
        }
    }

    private fun updateCharactersListLiveData(charactersList: List<CharacterInfo>?, charactersUrlList: List<String>) {
        this.charactersListLiveData.value = charactersList
        isLoading.value = false
        if (charactersList.isNullOrEmpty()) {
            updateIsNoCharacters()
        }
    }

    private fun updateLiveData(episode: LocationInfo?) {
        this.location.value = episode
        isLoading.value = false
    }

    private fun updateIsNoCharacters() {
        isNoCharacters.value = true
    }
//
//    private fun updateIsNoCharactersFound() {
//        isNoCharactersFound.value = true
//    }
//
//    private fun updateIsNotEnoughCharactersFound() {
//        isNotEnoughCharactersFound.value = true
//    }

}