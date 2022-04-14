package org.martellina.rickandmorty.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.martellina.rickandmorty.R
import org.martellina.rickandmorty.appComponent
import org.martellina.rickandmorty.databinding.FragmentEpisodeDetailBinding
import org.martellina.rickandmorty.di.factory.ViewModelEpisodeFactory
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.EpisodeInfo
import org.martellina.rickandmorty.ui.adapters.AdapterCharacter
import org.martellina.rickandmorty.ui.viewmodels.ViewModelEpisode
import javax.inject.Inject

private const val KEY_EPISODE = "key.episode"

class FragmentEpisodeDetail: Fragment() {

    private lateinit var binding: FragmentEpisodeDetailBinding
    private var episode: EpisodeInfo? = null
    private var adapterCharacter = AdapterCharacter { character ->
        val action = FragmentEpisodeDetailDirections.actionFragmentEpisodeDetailToFragmentCharacterDetail(character)
        findNavController().navigate(action)
    }
    private var charactersList = ArrayList<CharacterInfo>()
    private val args: FragmentEpisodeDetailArgs by navArgs()
    private var episodeId = 0

    @Inject
    lateinit var factory: ViewModelEpisodeFactory
    private val viewModelEpisode by viewModels<ViewModelEpisode>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodeDetailBinding.inflate(inflater, container, false)
        binding.textViewNoCharacters.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        episodeId = args.episode.id

        if (episode == null) {
            viewModelEpisode.getEpisodeById(episodeId)
        }

        observeLiveData()
        initializeRecyclerView()
        initializeSwipeRefreshLayout()

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeLiveData() {
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
        viewModelEpisode.isNoCharacters.observe(viewLifecycleOwner) {
            it.let {
                binding.recyclerviewCharactersInEpisode.apply { visibility = if (it) View.GONE else View.VISIBLE }
                binding.textViewNoCharacters.apply { visibility = if (it) View.VISIBLE else View.GONE }
            }
        }
        viewModelEpisode.isNotEnoughCharactersFound.observe(viewLifecycleOwner) {
            it.let {
                if (it) Toast.makeText(requireContext(), R.string.toast_more_characters, Toast.LENGTH_SHORT).show()
            }
        }
        viewModelEpisode.isNoDataFound.observe(viewLifecycleOwner) {
            it.let{
                if (it) Toast.makeText(requireContext(),R.string.no_data_toast,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(episode: EpisodeInfo?) {
        with(binding) {
            textViewEpisodeName.text = episode?.name
            textViewEpisodeNumber.text = episode?.episode
            textViewEpisodeAirDate.text = episode?.air_date
        }
        if (charactersList.isNullOrEmpty()) {
            getCharactersList(episode)
        }
    }

    private fun getCharactersList(episode: EpisodeInfo?) {
        episode?.characters?.let { viewModelEpisode.getCharactersById(it) }
        viewModelEpisode.charactersListLiveData.observe(viewLifecycleOwner) {
            it. let {
                charactersList = it as ArrayList<CharacterInfo>
                adapterCharacter.updateList(charactersList)
            }
        }
    }

    private fun initializeRecyclerView() {
        with(binding) {
            with(recyclerviewCharactersInEpisode) {
                layoutManager = StaggeredGridLayoutManager(2, 1)
                adapter = adapterCharacter
            }
        }
    }

    private fun initializeSwipeRefreshLayout() {
        binding.swipeRefreshLayoutEpisode.setOnRefreshListener {
            viewModelEpisode.getEpisodeById(episodeId)
            binding.swipeRefreshLayoutEpisode.isRefreshing = false
        }
    }

    companion object {
        fun newInstance(id: Int) : FragmentEpisodeDetail {
            return FragmentEpisodeDetail().also {
                it.arguments = Bundle() .apply {
                    putInt(KEY_EPISODE, id)
                }
            }
        }
    }
}