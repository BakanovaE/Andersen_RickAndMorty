package org.martellina.rickandmorty.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.martellina.rickandmorty.data.Repository
import org.martellina.rickandmorty.data.RepositoryImpl
import org.martellina.rickandmorty.network.model.EpisodeInfo
import org.martellina.rickandmorty.network.model.EpisodesFilter
import javax.inject.Inject

class ViewModelEpisodes: ViewModel() {

    var episodesList = MutableLiveData<List<EpisodeInfo>>()
    var isLoading = MutableLiveData<Boolean>()
    var pages = MutableLiveData<Int>()
    var filter = EpisodesFilter()
    var isEmpty = MutableLiveData<Boolean>()

    val repository = RepositoryImpl.get()

    fun getAllEpisodes(page: Int, filter: EpisodesFilter) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAllEpisodes(page, filter)
            launch(Dispatchers.Main) {
                updateEpisodesList(result?.results, filter)
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

    private fun updateEpisodesList(list: List<EpisodeInfo>?, filter: EpisodesFilter) {
            if (episodesList.value.isNullOrEmpty()) {
                episodesList.value = list
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

