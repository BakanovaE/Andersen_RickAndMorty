package org.martellina.rickandmorty.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.martellina.rickandmorty.data.Repository
import org.martellina.rickandmorty.network.model.EpisodeInfo
import org.martellina.rickandmorty.network.model.EpisodesFilter
import javax.inject.Inject

class ViewModelEpisodes @Inject constructor(private val repository: Repository): ViewModel() {

    var episodesList = MutableLiveData<List<EpisodeInfo>>()
    var isLoading = MutableLiveData<Boolean>()
    var pages = MutableLiveData<Int>()
    var filter = EpisodesFilter()
    var isEmptyFilteredResult = MutableLiveData<Boolean>()
    var isEmpty = MutableLiveData<Boolean>()

    fun getAllEpisodes(page: Int, filter: EpisodesFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAllEpisodes(page, filter)
            launch(Dispatchers.Main) {
                updateEpisodesList(result?.results)
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
                        isEmptyFilteredResult.value = true
                }
                updatePages(result?.info?.pages)
            }
        }
    }

    private fun updateEpisodesList(list: List<EpisodeInfo>?) {
            if (episodesList.value.isNullOrEmpty()) {
                if (list.isNullOrEmpty()) {
                    isEmpty.value = true
                } else {
                    episodesList.value = list
                }
            } else {
                if (list != null) {
                    for (episode in list) {
                        episodesList.value = episodesList.value?.plus(episode)
                    }
                }
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

