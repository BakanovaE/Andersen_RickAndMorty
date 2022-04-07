package org.martellina.rickandmorty.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.martellina.rickandmorty.databinding.FragmentEpisodeDetailBinding
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.EpisodeInfo
import org.martellina.rickandmorty.ui.Navigator
import org.martellina.rickandmorty.ui.adapters.AdapterCharacter
import org.martellina.rickandmorty.ui.viewmodels.ViewModelEpisode



private const val KEY_EPISODE = "key.episode"

class FragmentEpisodeDetail: Fragment() {

    private lateinit var binding: FragmentEpisodeDetailBinding
    private var episode: EpisodeInfo? = null
    private lateinit var viewModelEpisode: ViewModelEpisode
    private lateinit var navigator: Navigator
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var adapterCharacter: AdapterCharacter
    private var charactersList = ArrayList<CharacterInfo>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Navigator) {
            navigator = context
        } else {
            error("Host should implement Navigator")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = requireArguments().getInt(KEY_EPISODE)
        viewModelEpisode = ViewModelProvider(this)[ViewModelEpisode::class.java]
        viewModelEpisode.getEpisodeById(id)
        viewModelEpisode.episodeLiveData.observe(viewLifecycleOwner) {
            it.let {
                episode = it as EpisodeInfo
                updateUI(episode)
            }
        }
        viewModelEpisode.isLoading.observe(viewLifecycleOwner) {
            it.let {
                binding.progressBar.apply { visibility = if (it) View.VISIBLE else View.GONE }
            }
        }
    }

    private fun initializeRecyclerView(charactersList: ArrayList<CharacterInfo>) {
        layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerView.layoutManager = layoutManager
        adapterCharacter = AdapterCharacter() {
                character -> val fragmentCharacterDetails = FragmentCharacterDetail.newInstance(character)
                navigator.navigate(fragmentCharacterDetails)
        }
        recyclerView.adapter = adapterCharacter
    }

    private fun updateUI(episode: EpisodeInfo?) {
        binding.textViewEpisodeName.text = episode?.name
        binding.textViewEpisodeNumber.text = episode?.episode
        binding.textViewEpisodeAirDate.text = episode?.air_date
        binding.buttonBack.setOnClickListener {
            navigator.goBack()
        }
        recyclerView = binding.recyclerviewCharactersInEpisode
        episode?.characters?.let { viewModelEpisode.getCharactersById(it) }
        viewModelEpisode.charactersListLiveData.observe(viewLifecycleOwner) {
            it. let {
                charactersList = it as ArrayList<CharacterInfo>

                adapterCharacter.updateList(charactersList)
            }
        }
        initializeRecyclerView(charactersList)
    }

    companion object {
        fun newInstance(episodeInfo: EpisodeInfo) : FragmentEpisodeDetail {
            return FragmentEpisodeDetail().also {
                it.arguments = Bundle() .apply {
                    putInt(KEY_EPISODE, episodeInfo.id)
                } }
        }


    }
}