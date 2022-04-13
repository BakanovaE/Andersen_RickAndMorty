package org.martellina.rickandmorty.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.martellina.rickandmorty.data.Repository
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.EpisodeInfo
import javax.inject.Inject

class ViewModelEpisode @Inject constructor(private val repository: Repository): ViewModel() {

    var episodeLiveData = MutableLiveData<EpisodeInfo>()
    var charactersListLiveData = MutableLiveData<List<CharacterInfo>>()
    var isLoading = MutableLiveData<Boolean>()
    var isNoCharacters = MutableLiveData<Boolean>()
    var isNotEnoughCharactersFound = MutableLiveData<Boolean>()

    fun getEpisodeById(id: Int) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getEpisodeById(id)
            launch(Dispatchers.Main) {
                result?.let {
                    episodeLiveData.postValue(it)
                }
                updateEpisodeLiveData(result)
            }
        }
    }

    fun getCharactersById(charactersUrlList: List<String>) {
        if (charactersUrlList.isNullOrEmpty()) {
            updateIsNoCharacters()
        }else {
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = ArrayList<CharacterInfo>()
                for (characterUrl in charactersUrlList) {
                    val id = characterUrl.split("/").last().toInt()
                    val character = repository.getCharacterById(id)
                    character?.let { result.add(it) }
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
        if (charactersList != null) {
            if (charactersList.size < charactersUrlList.size) {
                updateIsNotEnoughCharactersFound()
            }
        }
    }

    private fun updateEpisodeLiveData(episode: EpisodeInfo?) {
        this.episodeLiveData.value = episode
        isLoading.value = false
    }

    private fun updateIsNoCharacters() {
        isNoCharacters.value = true
    }

    private fun updateIsNotEnoughCharactersFound() {
        isNotEnoughCharactersFound.value = true
    }

}