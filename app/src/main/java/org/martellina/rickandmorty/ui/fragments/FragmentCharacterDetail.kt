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
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import org.martellina.rickandmorty.R
import org.martellina.rickandmorty.appComponent
import org.martellina.rickandmorty.databinding.FragmentCharacterDetailBinding
import org.martellina.rickandmorty.di.factory.ViewModelCharacterFactory
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.EpisodeInfo
import org.martellina.rickandmorty.ui.Navigator
import org.martellina.rickandmorty.ui.adapters.AdapterEpisode
import org.martellina.rickandmorty.ui.viewmodels.ViewModelCharacter
import javax.inject.Inject

private const val KEY_CHARACTER = "key.character"

class FragmentCharacterDetail: Fragment(R.layout.fragment_character_detail) {

    private lateinit var binding: FragmentCharacterDetailBinding
    private var character: CharacterInfo? = null
    private var episodesList = ArrayList<EpisodeInfo>()
    private val adapterEpisode = AdapterEpisode { episode ->
        val action = FragmentCharacterDetailDirections.actionFragmentCharacterDetailToFragmentEpisodeDetail(episode)
        findNavController().navigate(action)
    }
    private val args: FragmentCharacterDetailArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelCharacterFactory
    private val viewModelCharacter by viewModels<ViewModelCharacter>(factoryProducer = { factory })
    private var characterId = 0

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        binding.textViewNoCharacters.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterId = args.character.id

        if(character == null) {
            viewModelCharacter.getCharacterById(characterId)
        }

        observeLiveData()
        initializeRecyclerView()
        initializeSwipeRefreshLayout()

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeLiveData() {
        viewModelCharacter.characterLiveData.observe(viewLifecycleOwner) {
            it.let {
                character = it as CharacterInfo
                updateUI(character)
            }
        }
        viewModelCharacter.isLoading.observe(viewLifecycleOwner) {
            it.let {
                binding.progressBar.apply { visibility = if (it) View.VISIBLE else View.GONE }
            }
        }
        viewModelCharacter.isNoEpisodes.observe(viewLifecycleOwner) {
            it.let {
                binding.recyclerviewEpisodesInCharacter.apply { visibility = if (it) View.GONE else View.VISIBLE }
                binding.textViewNoCharacters.apply { visibility = if (it) View.VISIBLE else View.GONE }
            }
        }
        viewModelCharacter.isNotEnoughEpisodesFound.observe(viewLifecycleOwner) {
            it.let {
                if (it) Toast.makeText(requireContext(), R.string.toast_more_episodes_in_character, Toast.LENGTH_SHORT).show()
            }
        }
        viewModelCharacter.isNoDataFound.observe(viewLifecycleOwner) {
            it.let{
                if (it) Toast.makeText(requireContext(),R.string.no_data_toast,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(character: CharacterInfo?) {
        with(binding) {
            textViewCharacterName.text = character?.name
            textViewCharacterStatus.text = character?.status
            textViewCharacterSpecies.text = character?.species
            textViewCharacterType.text = if (character?.type?.isEmpty() == true) "unknown" else character?.type
            textViewCharacterGender.text = character?.gender
            textViewCharacterOrigin.text = character?.origin?.name
            textViewCharacterLocation.text = character?.location?.name
            Picasso.get().load(character?.image).into(imageViewCharacter)
            textViewCharacterOrigin.setOnClickListener {
                if (character != null) {
                    if (character.origin.url.isNotEmpty()) {
                        val action = FragmentCharacterDetailDirections.actionFragmentCharacterDetailToFragmentLocationDetail(
                                character.origin.url.split("/").last().trim().toInt()
                            )
                        findNavController().navigate(action)
                    } else {
                        Toast.makeText(requireContext(), "Origin is unknown", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            textViewCharacterLocation.setOnClickListener {
                if (character != null) {
                    if (character.location.url.isNotEmpty()) {
                        val action = FragmentCharacterDetailDirections.actionFragmentCharacterDetailToFragmentLocationDetail(
                                character.location.url.split("/").last().trim().toInt()
                            )
                        findNavController().navigate(action)
                    } else {
                        Toast.makeText(requireContext(), "Location is unknown", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        if(episodesList.isNullOrEmpty()) {
            getEpisodesList(character)
        }
    }

    private fun getEpisodesList(character: CharacterInfo?) {
        character?.episode?.let { viewModelCharacter.getEpisodesById(it) }
        viewModelCharacter.episodesListLiveData.observe(viewLifecycleOwner) {
            it. let {
                episodesList = it as ArrayList<EpisodeInfo>
                adapterEpisode.updateList(episodesList)
            }
        }
    }

    private fun initializeRecyclerView() {
        with(binding) {
            with(recyclerviewEpisodesInCharacter) {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterEpisode
            }
        }
    }

    private fun initializeSwipeRefreshLayout() {
        binding.swipeRefreshLayoutCharacter.setOnRefreshListener {
            viewModelCharacter.getCharacterById(characterId)
            binding.swipeRefreshLayoutCharacter.isRefreshing = false
        }
    }

    companion object {
        fun newInstance(character: CharacterInfo) : FragmentCharacterDetail {
            return FragmentCharacterDetail().also {
                it.arguments = Bundle() .apply {
                    putInt(KEY_CHARACTER, character.id)
                }
            }
        }
    }
}