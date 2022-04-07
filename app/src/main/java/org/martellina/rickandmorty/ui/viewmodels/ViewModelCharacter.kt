package org.martellina.rickandmorty.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.martellina.rickandmorty.network.Repository
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.EpisodeInfo

class ViewModelCharacter: ViewModel() {

    var character = MutableLiveData<CharacterInfo>()
    var episodesListLiveData = MutableLiveData<List<EpisodeInfo>>()
    var isLoading = MutableLiveData<Boolean>()

    private val repository = Repository.get()

    fun getCharacterById(id: Int) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCharacterById(id)
            launch(Dispatchers.Main) {
                updateLiveData(result)
            }
        }
    }

    fun getEpisodesById(episodesList: List<String>) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = ArrayList<EpisodeInfo>()
            for (episodeUrl in episodesList) {
                val id = episodeUrl.split("/").last().toInt()
                val episode = repository.getEpisodeById(id)
                episode?.let { result.add(it) }
            }
            launch(Dispatchers.Main) {
                updateEpisodesListLiveData(result)
            }
        }
    }

    private fun updateEpisodesListLiveData(episodesList: List<EpisodeInfo>?) {
        this.episodesListLiveData.value = episodesList
        isLoading.value = false
    }

    private fun updateLiveData(character: CharacterInfo?) {
        this.character.value = character
        isLoading.value = false
    }

}