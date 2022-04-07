package org.martellina.rickandmorty.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.martellina.rickandmorty.databinding.FragmentEpisodesBinding
import org.martellina.rickandmorty.network.model.EpisodeInfo
import org.martellina.rickandmorty.network.model.EpisodesFilter
import org.martellina.rickandmorty.ui.Navigator
import org.martellina.rickandmorty.ui.adapters.AdapterEpisodes
import org.martellina.rickandmorty.ui.viewmodels.ViewModelEpisodes

class FragmentEpisodes: Fragment() {

    private lateinit var binding: FragmentEpisodesBinding
    private var adapterEpisodes = AdapterEpisodes {
            episode -> val fragmentEpisodeDetail = FragmentEpisodeDetail.newInstance(episode)
        navigator.navigate(fragmentEpisodeDetail)
    }
    private lateinit var recyclerViewEpisodes: RecyclerView
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private var episodesList = ArrayList<EpisodeInfo>()
    private lateinit var viewModelEpisodes: ViewModelEpisodes
    private lateinit var navigator: Navigator
    private var filter = EpisodesFilter()
    private var page = 1
    private var pages = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is Navigator) {
            navigator = context
        } else {
            error("Host should implements Navigator")
        }
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

        recyclerViewEpisodes = binding.recyclerviewEpisodes
        initializeRecyclerView()

        viewModelEpisodes = ViewModelProvider(this)[ViewModelEpisodes::class.java]
        observeLiveData()

        if (episodesList.isNullOrEmpty()) {
            page = 1
            viewModelEpisodes.getAllEpisodes(page, filter)
        }

        binding.filterButtonEpisodes.setOnClickListener {
            FragmentFilterEpisodes.newInstance(filter)
                .show(childFragmentManager, "EpisodesDialog")
        }
        binding.swipeRefreshLayoutEpisodes.setOnRefreshListener {
            viewModelEpisodes.getFilteredEpisodes(filter)
            binding.swipeRefreshLayoutEpisodes.isRefreshing = false
        }
        binding.clearFilterButtonEpisodes.setOnClickListener {
            filter = EpisodesFilter("", "")
            viewModelEpisodes.getFilteredEpisodes(filter)
        }
    }

    private fun initializeRecyclerView() {
        layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerViewEpisodes.layoutManager = layoutManager
        recyclerViewEpisodes.adapter = adapterEpisodes
        initializePagination()
    }

    private fun initializePagination() {
        recyclerViewEpisodes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    viewModelEpisodes.getAllEpisodes(page, filter)
                }
            }
        })
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

        viewModelEpisodes.isEmpty.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "No episodes found, check filter or/and connect to network for loading more data", Toast.LENGTH_LONG).show()
        }
    }

    fun getFilteredEpisodes(filter: EpisodesFilter) {
        viewModelEpisodes.getFilteredEpisodes(filter)
        this.filter = filter
    }

    companion object {
        fun newInstance() = FragmentEpisodes()
    }
}