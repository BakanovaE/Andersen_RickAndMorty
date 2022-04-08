package org.martellina.rickandmorty.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.martellina.rickandmorty.network.Repository
import org.martellina.rickandmorty.network.model.EpisodeInfo
import org.martellina.rickandmorty.network.model.EpisodesFilter

class ViewModelEpisodes : ViewModel() {

    var episodesList = MutableLiveData<List<EpisodeInfo>>()
    var isLoading = MutableLiveData<Boolean>()
    var pages = MutableLiveData<Int>()
    var filter = EpisodesFilter()
    var isEmpty = MutableLiveData<Boolean>()

    private val repository = Repository.get()

    fun getAllEpisodes(page: Int, filter: EpisodesFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAllEpisodes(page, filter)
            launch(Dispatchers.Main) {
                updateLiveData(result?.results, filter)
                updatePages(result?.info?.pages)
            }
        }
    }

    fun getFilteredEpisodes(filter: EpisodesFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getFilteredEpisodes(filter)
            launch(Dispatchers.Main) {
                setFilteredList(result?.results)
                if (result != null) {
                    if (result.results.isEmpty())
                        isEmpty.value = true
                }
                updatePages(result?.info?.pages)
            }
        }
    }

    private fun updateLiveData(list: List<EpisodeInfo>?, filter: EpisodesFilter) {
        if(filter.name.isNullOrEmpty() || filter.code.isNullOrEmpty()) {
            if (episodesList.value.isNullOrEmpty()) {
                episodesList.value = list
            } else {
                if (list != null) {
                    for (episode in list) {
                        episodesList.value = episodesList.value?.plus(episode)
                    }
                }
            }
        } else {
            episodesList.value = list
        }
        isLoading.value = false
    }

    private fun setFilteredList(list: List<EpisodeInfo>?) {
        episodesList.value = list
        isLoading.value = false
    }

    private fun updatePages(pages: Int?) {
        this.pages.value = pages
    }

}

