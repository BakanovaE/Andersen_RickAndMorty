package org.martellina.rickandmorty.ui.fragments

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.martellina.rickandmorty.R
import org.martellina.rickandmorty.appComponent
import org.martellina.rickandmorty.databinding.FragmentLocationDetailBinding
import org.martellina.rickandmorty.di.factory.ViewModelEpisodesFactory
import org.martellina.rickandmorty.di.factory.ViewModelLocationFactory
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.LocationInfo
import org.martellina.rickandmorty.ui.Navigator
import org.martellina.rickandmorty.ui.adapters.AdapterCharacter
import org.martellina.rickandmorty.ui.viewmodels.ViewModelEpisodes
import org.martellina.rickandmorty.ui.viewmodels.ViewModelLocation
import javax.inject.Inject

private const val KEY_LOCATION = "key.location"

class FragmentLocationDetail: Fragment() {

    private lateinit var binding: FragmentLocationDetailBinding
    private var location: LocationInfo? = null
    private lateinit var navigator: Navigator
    private var adapterCharacter = AdapterCharacter {
            character -> val fragmentCharacterDetails = FragmentCharacterDetail.newInstance(character)
        navigator.navigate(fragmentCharacterDetails)
    }
    private var charactersList = ArrayList<CharacterInfo>()

    @Inject
    lateinit var factory: ViewModelLocationFactory
    private val viewModelLocation by viewModels<ViewModelLocation>(factoryProducer = { factory })

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
        binding = FragmentLocationDetailBinding.inflate(inflater, container, false)
        binding.textViewNoCharacters.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = requireArguments().getInt(KEY_LOCATION)

        if (location == null) {
            viewModelLocation.getLocationById(id)
        }

        observeLiveData()

        initializeRecyclerView()
    }

    private fun observeLiveData() {
        viewModelLocation.location.observe(viewLifecycleOwner) {
            it.let {
                location = it as LocationInfo
                updateUI(location)
            }
        }
        viewModelLocation.isLoading.observe(viewLifecycleOwner) {
            it.let {
                binding.progressBar.apply { visibility = if (it) View.VISIBLE else View.GONE }
            }
        }
    }

    private fun updateUI(location: LocationInfo?) {
        with(binding) {
            textViewLocationName.text = location?.name
            textViewLocationType.text =
                if (location?.type?.isEmpty() == true) "unknown" else location?.type
            textViewLocationDimension.text =
                if (location?.dimension?.isEmpty() == true) "unknown" else location?.dimension
            buttonBack.setOnClickListener {
                navigator.goBack()
            }
        }
        if (location?.characters.isNullOrEmpty()) {
            binding.recyclerviewCharactersInLocation.visibility = View.GONE
            binding.textViewNoCharacters.visibility = View.VISIBLE

        } else {
            if (charactersList.isNullOrEmpty()) {
                getCharactersList(location)
            }
        }
    }

    private fun getCharactersList(location: LocationInfo?) {
        location?.characters?.let { viewModelLocation.getCharactersById(it) }
        viewModelLocation.charactersListLiveData.observe(viewLifecycleOwner) {
            it. let {
                charactersList = it as ArrayList<CharacterInfo>
                adapterCharacter.updateList(charactersList)
            }
        }
    }

    private fun initializeRecyclerView() {
        with(binding) {
            with(recyclerviewCharactersInLocation) {
                layoutManager = StaggeredGridLayoutManager(2, 1)
                adapter = adapterCharacter
            }
        }
    }

    companion object {
        fun newInstance(id: Int) : FragmentLocationDetail {
            return FragmentLocationDetail().also {
                it.arguments = Bundle() .apply {
                    putInt(KEY_LOCATION, id)
                }
            }
        }
    }
}