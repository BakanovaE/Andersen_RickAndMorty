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
import org.martellina.rickandmorty.databinding.FragmentCharactersBinding
import org.martellina.rickandmorty.di.factory.ViewModelCharactersFactory
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.CharactersFilter
import org.martellina.rickandmorty.ui.adapters.AdapterCharacters
import org.martellina.rickandmorty.ui.viewmodels.ViewModelCharacters
import javax.inject.Inject

class FragmentCharacters: Fragment(R.layout.fragment_characters) {

    private lateinit var binding: FragmentCharactersBinding
    private var charactersList = ArrayList<CharacterInfo>()
    private var filter = CharactersFilter()
    private var page = 1
    private var pages = 1
    private var orientation = 0
    private var adapterCharacters = AdapterCharacters { character ->
        val action =
            FragmentCharactersDirections.actionFragmentCharactersToFragmentCharacterDetail(character)
        findNavController().navigate(action)
    }

    @Inject
    lateinit var factory: ViewModelCharactersFactory
    val viewModelCharacters by viewModels<ViewModelCharacters>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orientation = resources.configuration.orientation
        setBackground(orientation)

        observeLiveData()

        initializeRecyclerView()

        if (charactersList.isNullOrEmpty()) {
            page = 1
            viewModelCharacters.getAllCharacters(page, filter)
        }

        binding.filterButtonCharacters.setOnClickListener {
            FragmentFilterCharacters.newInstance(filter)
                .show(childFragmentManager, "CharactersDialog")
        }

        binding.swipeRefreshLayoutCharacters.setOnRefreshListener {
            viewModelCharacters.reloadCharacters(page, filter)
            binding.swipeRefreshLayoutCharacters.isRefreshing = false
        }

        binding.clearFilterButtonCharacters.setOnClickListener {
            clearFilter()
            viewModelCharacters.getFilteredCharacters(CharactersFilter(null, null, null, null, null))
        }
    }

    private fun observeLiveData() {
        viewModelCharacters.charactersList.observe(viewLifecycleOwner) {
            it.let {
                charactersList = it as ArrayList<CharacterInfo>
                adapterCharacters.updateList(charactersList)
            }
        }
        viewModelCharacters.isLoading.observe(viewLifecycleOwner) {
            it.let {
                binding.progressBar.apply { visibility = if (it) View.VISIBLE else View.GONE }
            }
        }
        viewModelCharacters.pages.observe(viewLifecycleOwner) {
            this.pages = it
        }
        viewModelCharacters.isEmptyFilteredResult.observe(viewLifecycleOwner) {
            it.let {
                if (it)
            Toast.makeText(requireContext(), R.string.toast_filter_characters, Toast.LENGTH_SHORT).show()
            }
        }
        viewModelCharacters.isEmpty.observe(viewLifecycleOwner) {
            it.let {
                if (it)
                    Toast.makeText(requireContext(), R.string.toast_empty_list, Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }

    private fun initializeRecyclerView() {
        with(binding) {
            with(recyclerviewCharacters) {
                layoutManager = StaggeredGridLayoutManager(2, 1)
                adapter = adapterCharacters
            }
        }
        initializePagination()
    }

    private fun initializePagination() {
        binding.recyclerviewCharacters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    viewModelCharacters.getAllCharacters(page, filter)
                }
            }
        })
    }

    fun getFilteredCharacters(filter: CharactersFilter) {
        this.filter = filter
        viewModelCharacters.getFilteredCharacters(filter)
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

    private fun clearFilter() {
        filter = CharactersFilter(null, null, null, null, null)
    }

    companion object {
        fun newInstance() = FragmentCharacters()
    }
}