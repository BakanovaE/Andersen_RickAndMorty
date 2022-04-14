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

class ViewModelCharacter @Inject constructor(private val repository: Repository): ViewModel() {

    var characterLiveData = MutableLiveData<CharacterInfo>()
    var episodesListLiveData = MutableLiveData<List<EpisodeInfo>>()
    var isLoading = MutableLiveData<Boolean>()
    var isNoEpisodes = MutableLiveData<Boolean>()
    var isNotEnoughEpisodesFound = MutableLiveData<Boolean>()
    var isNoDataFound = MutableLiveData<Boolean>()

    fun getCharacterById(id: Int) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCharacterById(id)
            launch(Dispatchers.Main) {
                result?.let {characterLiveData.postValue(it)} ?: run{isNoDataFound.value = true}
                isLoading.value = false
            }
        }
    }

    fun getEpisodesById(episodesUrlList: List<String>) {
        if (episodesUrlList.isNullOrEmpty()) {
            updateIsNoEpisodes()
        } else {
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = ArrayList<EpisodeInfo>()
                for (episodeUrl in episodesUrlList) {
                    val id = episodeUrl.split("/").last().toInt()
                    val episode = repository.getEpisodeById(id)
                    episode?.let { result.add(it) }
                }
                launch(Dispatchers.Main) {
                    updateEpisodesListLiveData(result, episodesUrlList)
                }
            }
        }
    }

    private fun updateEpisodesListLiveData(episodesList: List<EpisodeInfo>?, episodesUrlList: List<String>) {
        this.episodesListLiveData.value = episodesList
        isLoading.value = false
        if (episodesList != null) {
            if (episodesList.size < episodesUrlList.size) {
                updateIsNotEnoughEpisodesFound()
            }
        }
    }

    private fun updateIsNoEpisodes() {
        isNoEpisodes.value = true
    }

    private fun updateIsNotEnoughEpisodesFound() {
        isNotEnoughEpisodesFound.value = true
    }

}