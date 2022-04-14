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
    var isNotEnoughCharactersFound = MutableLiveData<Boolean>()
    var isNoDataFound = MutableLiveData<Boolean>()

    fun getLocationById(id: Int) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getLocationById(id)
            launch(Dispatchers.Main) {
                result?.let {location.postValue(it)} ?: run {isNoDataFound.value = true}
                isLoading.value = false
            }
        }
    }

    fun getCharactersById(charactersUrlList: List<String>) {
        if (charactersUrlList.isNullOrEmpty()) {
            updateIsNoCharacters()
        } else {
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
    }

    private fun updateCharactersListLiveData(charactersList: List<CharacterInfo>?, charactersUrlList: List<String>) {
        this.charactersListLiveData.value = charactersList
        isLoading.value = false
        charactersList?.let {
            if (it.size < charactersUrlList.size) {
                updateIsNotEnoughCharactersFound()
            }
        }
    }

    private fun updateIsNoCharacters() {
        isNoCharacters.value = true
    }

    private fun updateIsNotEnoughCharactersFound() {
        isNotEnoughCharactersFound.value = true
    }
}