package org.martellina.rickandmorty.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    private lateinit var navigator: Navigator
    private var episodesList = ArrayList<EpisodeInfo>()
    private val adapterEpisode = AdapterEpisode {
            episode -> val fragmentEpisodeDetail = FragmentEpisodeDetail.newInstance(episode)
        navigator.navigate(fragmentEpisodeDetail) }

    @Inject
    lateinit var factory: ViewModelCharacterFactory
    private val viewModelCharacter by viewModels<ViewModelCharacter>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
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
        binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = requireArguments().getInt(KEY_CHARACTER)

        if(character == null) {
            viewModelCharacter.getCharacterById(id)
        }

        observeLiveData()
        initializeRecyclerView()
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
            buttonBack.setOnClickListener {
                navigator.goBack()
            }
            textViewCharacterOrigin.setOnClickListener {
                if (character != null) {
                    if (character.origin.url.isNotEmpty()) {
                        navigator.navigate(
                            FragmentLocationDetail.newInstance(
                                character.origin.url.split("/").last().trim().toInt()
                            )
                        )
                    } else {
                        Toast.makeText(requireContext(), "Origin is unknown", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            textViewCharacterLocation.setOnClickListener {
                if (character != null) {
                    if (character.location.url.isNotEmpty()) {
                        navigator.navigate(
                            FragmentLocationDetail.newInstance(character.location.url.split("/").last().trim().toInt())
                        )
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