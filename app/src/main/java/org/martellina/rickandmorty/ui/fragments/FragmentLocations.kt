package org.martellina.rickandmorty.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.martellina.rickandmorty.appComponent
import org.martellina.rickandmorty.ui.adapters.AdapterLocations
import org.martellina.rickandmorty.databinding.FragmentLocationsBinding
import org.martellina.rickandmorty.di.factory.ViewModelEpisodesFactory
import org.martellina.rickandmorty.di.factory.ViewModelLocationsFactory
import org.martellina.rickandmorty.network.model.EpisodesFilter
import org.martellina.rickandmorty.network.model.LocationInfo
import org.martellina.rickandmorty.network.model.LocationsFilter
import org.martellina.rickandmorty.ui.Navigator
import org.martellina.rickandmorty.ui.viewmodels.ViewModelEpisodes
import org.martellina.rickandmorty.ui.viewmodels.ViewModelLocations
import javax.inject.Inject

class FragmentLocations: Fragment() {

    private lateinit var binding: FragmentLocationsBinding
    private var adapterLocations = AdapterLocations {
            location -> val fragmentLocationDetail = FragmentLocationDetail.newInstance(location.id)
        navigator.navigate(fragmentLocationDetail)
    }
    private lateinit var recyclerViewLocations: RecyclerView
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private var locationsList = ArrayList<LocationInfo>()
    private lateinit var navigator: Navigator
    private var filter = LocationsFilter()
    private var page = 1
    private var pages = 1

    @Inject
    lateinit var factory: ViewModelLocationsFactory

    val viewModelLocations by viewModels<ViewModelLocations>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
        if (context is Navigator) {
            navigator = context
        } else {
            error ("Host should implements Navigator")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()

        recyclerViewLocations = binding.recyclerviewLocations
        initializeRecyclerView()

        if (locationsList.isNullOrEmpty()) {
            page = 1
            viewModelLocations.getAllLocations(page, filter)
        }

        binding.filterButtonLocations.setOnClickListener {
            FragmentFilterLocations.newInstance(filter)
                .show(childFragmentManager, "LocationsDialog")
        }

        binding.swipeRefreshLayoutLocations.setOnRefreshListener {
            viewModelLocations.getFilteredLocations(filter)
            binding.swipeRefreshLayoutLocations.isRefreshing = false
        }

        binding.clearFilterButtonLocations.setOnClickListener {
            filter = LocationsFilter("", "", "")
            viewModelLocations.getFilteredLocations(filter)
        }
    }

    private fun observeLiveData() {
        viewModelLocations.locationsList.observe(viewLifecycleOwner) {
            it.let {
                locationsList = it as ArrayList<LocationInfo>
                adapterLocations.updateList(locationsList)
            }
        }

        viewModelLocations.isLoading.observe(viewLifecycleOwner) {
            it.let {
                binding.progressBar.apply { visibility = if (it) View.VISIBLE else View.GONE }
            }
        }

        viewModelLocations.pages.observe(viewLifecycleOwner) {
            this.pages = it
        }

        viewModelLocations.isEmpty.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "No locations found, check filter or/and connect to network for loading more data", Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeRecyclerView() {
        layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerViewLocations.layoutManager = layoutManager
        recyclerViewLocations.adapter = adapterLocations
        initializePagination()
    }

    private fun initializePagination() {
        recyclerViewLocations.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    viewModelLocations.getAllLocations(page, filter)
                }
            }
        })
    }


    fun getFilteredLocations(filter: LocationsFilter) {
        viewModelLocations.getFilteredLocations(filter)
        this.filter = filter
    }


    companion object {
        fun newInstance() = FragmentLocations()
    }
}