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
import org.martellina.rickandmorty.databinding.FragmentEpisodesBinding
import org.martellina.rickandmorty.di.factory.ViewModelEpisodesFactory
import org.martellina.rickandmorty.network.model.EpisodeInfo
import org.martellina.rickandmorty.network.model.EpisodesFilter
import org.martellina.rickandmorty.ui.adapters.AdapterEpisodes
import org.martellina.rickandmorty.ui.viewmodels.ViewModelEpisodes
import javax.inject.Inject

class FragmentEpisodes: Fragment() {

    private lateinit var binding: FragmentEpisodesBinding
    private var adapterEpisodes = AdapterEpisodes { episode ->
        val action = FragmentEpisodesDirections.actionFragmentEpisodesToFragmentEpisodeDetail(episode)
        findNavController().navigate(action)
    }
    private var episodesList = ArrayList<EpisodeInfo>()
    private var filter = EpisodesFilter()
    private var page = 1
    private var pages = 1
    private var orientation = 0

    @Inject
    lateinit var factory: ViewModelEpisodesFactory
    val viewModelEpisodes by viewModels<ViewModelEpisodes>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orientation = resources.configuration.orientation
        setBackground(orientation)

        if (episodesList.isNullOrEmpty()) {
            page = 1
            viewModelEpisodes.getAllEpisodes(page, filter)
        }

        initializeRecyclerView()
        initializePagination()
        initializeButtons()
        initializeSwipeRefreshLayout()
        observeLiveData()
    }

    private fun initializeRecyclerView() {
        with(binding) {
            recyclerviewEpisodes.layoutManager = StaggeredGridLayoutManager(2, 1)
            recyclerviewEpisodes.adapter = adapterEpisodes
        }
    }

    private fun initializePagination() {
        binding.recyclerviewEpisodes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    viewModelEpisodes.getAllEpisodes(page, filter)
                }
            }
        })
    }

    private fun initializeSwipeRefreshLayout() {
        binding.swipeRefreshLayoutEpisodes.setOnRefreshListener {
            viewModelEpisodes.reloadEpisodes(1, filter)
            binding.swipeRefreshLayoutEpisodes.isRefreshing = false
        }
    }

    private fun initializeButtons() {
        with(binding) {
            filterButtonEpisodes.setOnClickListener {
                FragmentFilterEpisodes.newInstance(filter)
                    .show(childFragmentManager, "EpisodesDialog")
            }

            clearFilterButtonEpisodes.setOnClickListener {
                filter = EpisodesFilter(null, null)
                viewModelEpisodes.getFilteredEpisodes(filter)
            }
        }
    }

    private fun observeLiveData() {
        viewModelEpisodes.episodesList.observe(viewLifecycleOwner) {
            it.let {
                episodesList = it as ArrayList<EpisodeInfo>
                adapterEpisodes.updateList(episodesList)
            }
        }
        viewModelEpisodes.isLoading.observe(viewLifecycleOwner) {
            it.let {
                binding.progressBar.apply { visibility = if (it) View.VISIBLE else View.GONE }
            }
        }
        viewModelEpisodes.pages.observe(viewLifecycleOwner) {
            this.pages = it
        }
        viewModelEpisodes.isEmptyFilteredResult.observe(viewLifecycleOwner) {
            it.let {
                if (it) Toast.makeText(requireContext(), R.string.toast_filter_episodes, Toast.LENGTH_SHORT).show()
            }
        }
        viewModelEpisodes.isEmpty.observe(viewLifecycleOwner) {
            it.let {
                if (it) Toast.makeText(requireContext(), R.string.toast_empty_list, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getFilteredEpisodes(filter: EpisodesFilter) {
        viewModelEpisodes.getFilteredEpisodes(filter)
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
        fun newInstance() = FragmentEpisodes()
    }
}