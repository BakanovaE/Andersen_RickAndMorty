package org.martellina.rickandmorty.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.martellina.rickandmorty.R
import org.martellina.rickandmorty.appComponent
import org.martellina.rickandmorty.ui.adapters.AdapterLocations
import org.martellina.rickandmorty.databinding.FragmentLocationsBinding
import org.martellina.rickandmorty.di.factory.ViewModelLocationsFactory
import org.martellina.rickandmorty.network.model.LocationInfo
import org.martellina.rickandmorty.network.model.LocationsFilter
import org.martellina.rickandmorty.ui.viewmodels.ViewModelLocations
import javax.inject.Inject

class FragmentLocations: Fragment() {

    private lateinit var binding: FragmentLocationsBinding
    private var adapterLocations = AdapterLocations {location ->
        val action = FragmentLocationsDirections.actionFragmentLocationsToFragmentLocationDetail(location.id)
        findNavController().navigate(action)
    }
    private var locationsList = ArrayList<LocationInfo>()
    private var filter = LocationsFilter()
    private var page = 1
    private var pages = 1
    private var orientation = 0


    @Inject
    lateinit var factory: ViewModelLocationsFactory
    val viewModelLocations by viewModels<ViewModelLocations>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
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

        orientation = resources.configuration.orientation
        setBackground(orientation)

        if (locationsList.isNullOrEmpty()) {
            page = 1
            viewModelLocations.getAllLocations(page, filter)
        }

        initializeRecyclerView()
        initializePagination()
        initializeButtons()
        initializeSwipeRefreshLayout()
        observeLiveData()
    }

    private fun initializeRecyclerView() {
        with(binding) {
            with(recyclerviewLocations) {
                layoutManager = StaggeredGridLayoutManager(2, 1)
                adapter = adapterLocations
            }
        }
    }

    private fun initializePagination() {
        binding.recyclerviewLocations.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    viewModelLocations.getAllLocations(page, filter)
                }
            }
        })
    }

    private fun initializeButtons() {
        with(binding) {
            filterButtonLocations.setOnClickListener {
                FragmentFilterLocations.newInstance(filter)
                    .show(childFragmentManager, "LocationsDialog")
            }
            clearFilterButtonLocations.setOnClickListener {
                filter = LocationsFilter(null, null, null)
                viewModelLocations.getFilteredLocations(filter)
            }
        }
    }

    private fun initializeSwipeRefreshLayout() {
        binding.swipeRefreshLayoutLocations.setOnRefreshListener {
            viewModelLocations.reloadLocations(page, filter)
            binding.swipeRefreshLayoutLocations.isRefreshing = false
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

        viewModelLocations.isEmptyFilteredResult.observe(viewLifecycleOwner) {
            it.let {
                if (it) Toast.makeText(requireContext(), R.string.toast_filter_locations, Toast.LENGTH_SHORT).show()
            }
        }
        viewModelLocations.isEmpty.observe(viewLifecycleOwner) {
            it.let {
                if (it) Toast.makeText(requireContext(), R.string.toast_empty_list, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getFilteredLocations(filter: LocationsFilter) {
        viewModelLocations.getFilteredLocations(filter)
        this.filter = filter
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        orientation = newConfig.orientation
        setBackground(orientation)
    }

    private fun setBackground(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            binding.root.setBackgroundResource(R.drawable.background_land_jpg)
        else if (orientation == Configuration.ORIENTATION_PORTRAIT)
            binding.root.setBackgroundResource(R.drawable.background_jpg)
    }

    override fun onResume() {
        super.onResume()
        setBackground(orientation)
    }

    companion object {
        fun newInstance() = FragmentLocations()
    }
}