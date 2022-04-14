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
import org.martellina.rickandmorty.databinding.FragmentLocationDetailBinding
import org.martellina.rickandmorty.di.factory.ViewModelLocationFactory
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.LocationInfo
import org.martellina.rickandmorty.ui.adapters.AdapterCharacter
import org.martellina.rickandmorty.ui.viewmodels.ViewModelLocation
import javax.inject.Inject

private const val KEY_LOCATION = "key.location"

class FragmentLocationDetail: Fragment() {

    private lateinit var binding: FragmentLocationDetailBinding
    private var location: LocationInfo? = null
    private var adapterCharacter = AdapterCharacter {character ->
        val action = FragmentLocationDetailDirections.actionFragmentLocationDetailToFragmentCharacterDetail(character)
        findNavController().navigate(action)
    }
    private var charactersList = ArrayList<CharacterInfo>()
    private var locationId = 0
    private val args: FragmentLocationDetailArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelLocationFactory
    private val viewModelLocation by viewModels<ViewModelLocation>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
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

        locationId = args.locationId

        if (location == null) {
            viewModelLocation.getLocationById(locationId)
        }

        observeLiveData()
        initializeRecyclerView()
        initializeSwipeRefreshLayout()

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
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
        viewModelLocation.isNoCharacters.observe(viewLifecycleOwner) {
            it.let {
                binding.recyclerviewCharactersInLocation.apply { visibility = if (it) View.GONE else View.VISIBLE }
                binding.textViewNoCharacters.apply { visibility = if (it) View.VISIBLE else View.GONE }
            }
        }
        viewModelLocation.isNotEnoughCharactersFound.observe(viewLifecycleOwner) {
            it.let {
                if (it) Toast.makeText(requireContext(),R.string.toast_more_characters,Toast.LENGTH_SHORT).show()
            }
        }
        viewModelLocation.isNoDataFound.observe(viewLifecycleOwner) {
            it.let{
                if (it) Toast.makeText(requireContext(),R.string.no_data_toast,Toast.LENGTH_SHORT).show()
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
        }
            if (charactersList.isNullOrEmpty()) {
                getCharactersList(location)
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

    private fun initializeSwipeRefreshLayout() {
        binding.swipeRefreshLayoutLocation.setOnRefreshListener {
            viewModelLocation.getLocationById(locationId)
            binding.swipeRefreshLayoutLocation.isRefreshing = false
        }
    }

    companion object {
        fun newInstance(locationId: Int) : FragmentLocationDetail {
            return FragmentLocationDetail().also {
                it.arguments = Bundle() .apply {
                    putInt(KEY_LOCATION, locationId)
                }
            }
        }
    }
}