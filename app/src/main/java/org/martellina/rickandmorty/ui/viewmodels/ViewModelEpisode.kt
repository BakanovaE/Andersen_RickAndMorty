package org.martellina.rickandmorty.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.martellina.rickandmorty.network.Repository
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.EpisodeInfo

class ViewModelEpisode: ViewModel() {

    var episodeLiveData = MutableLiveData<EpisodeInfo>()
    var charactersListLiveData = MutableLiveData<List<CharacterInfo>>()
    var isLoading = MutableLiveData<Boolean>()

    private val repository = Repository.get()

    fun getEpisodeById(id: Int) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getEpisodeById(id)
            launch(Dispatchers.Main) {
                episodeLiveData.postValue(result)
                updateLiveData(result)
            }
        }
    }

    fun getCharactersById(charactersList: List<String>) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = ArrayList<CharacterInfo>()
            for (characterUrl in charactersList) {
                val id = characterUrl.split("/").last().toInt()
                val character = repository.getCharacterById(id)
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

    private fun updateLiveData(episode: EpisodeInfo?) {
        this.episodeLiveData.value = episode
        isLoading.value = false
    }

}