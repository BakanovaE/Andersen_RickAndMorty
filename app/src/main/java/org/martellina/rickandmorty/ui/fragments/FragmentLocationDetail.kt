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
import org.martellina.rickandmorty.databinding.FragmentLocationDetailBinding
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.LocationInfo
import org.martellina.rickandmorty.ui.Navigator
import org.martellina.rickandmorty.ui.adapters.AdapterCharacter
import org.martellina.rickandmorty.ui.viewmodels.ViewModelLocation

private const val KEY_LOCATION = "key.location"

class FragmentLocationDetail: Fragment() {

    private lateinit var binding: FragmentLocationDetailBinding
    private var location: LocationInfo? = null
    private lateinit var viewModelLocation: ViewModelLocation
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
        binding = FragmentLocationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = requireArguments().getInt(KEY_LOCATION)
        viewModelLocation = ViewModelProvider(this).get(ViewModelLocation::class.java)
        viewModelLocation.getLocationById(id)
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

    private fun initializeRecyclerView(charactersList: ArrayList<CharacterInfo>) {
        layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerView.layoutManager = layoutManager
        adapterCharacter = AdapterCharacter() {
                character -> val fragmentCharacterDetails = FragmentCharacterDetail.newInstance(character)
            navigator.navigate(fragmentCharacterDetails)
        }
        recyclerView.adapter = adapterCharacter
    }

    private fun updateUI(location: LocationInfo?) {
        binding.textViewLocationName.text = location?.name
        binding.textViewLocationType.text = if (location?.type?.isEmpty() == true) "unknown" else location?.type
        binding.textViewLocationDimension.text = if (location?.dimension?.isEmpty() == true) "unknown" else location?.dimension
        binding.buttonBack.setOnClickListener {
            navigator.goBack()
        }

        recyclerView = binding.recyclerviewCharactersInLocation
        location?.characters?.let { viewModelLocation.getCharactersById(it) }
        viewModelLocation.charactersListLiveData.observe(viewLifecycleOwner) {
            it. let {
                charactersList = it as ArrayList<CharacterInfo>

                adapterCharacter.updateList(charactersList)
            }
        }
        initializeRecyclerView(charactersList)
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