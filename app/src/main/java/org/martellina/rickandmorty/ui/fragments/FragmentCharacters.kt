package org.martellina.rickandmorty.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.martellina.rickandmorty.R
import org.martellina.rickandmorty.appComponent
import org.martellina.rickandmorty.databinding.FragmentCharactersBinding
import org.martellina.rickandmorty.di.factory.ViewModelCharactersFactory
import org.martellina.rickandmorty.di.factory.ViewModelEpisodesFactory
import org.martellina.rickandmorty.network.model.CharacterInfo
import org.martellina.rickandmorty.network.model.CharactersFilter
import org.martellina.rickandmorty.ui.Navigator
import org.martellina.rickandmorty.ui.adapters.AdapterCharacters
import org.martellina.rickandmorty.ui.viewmodels.ViewModelCharacters
import javax.inject.Inject

class FragmentCharacters: Fragment(R.layout.fragment_characters) {

    private lateinit var binding: FragmentCharactersBinding
    private lateinit var adapterCharacters: AdapterCharacters
    private lateinit var recyclerViewCharacters: RecyclerView
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private var charactersList = ArrayList<CharacterInfo>()
//    private lateinit var viewModelCharacters: ViewModelCharacters
    private lateinit var navigator: Navigator
    private var filter = CharactersFilter()
    private var page = 1
    private var pages = 1

    @Inject
    lateinit var factory: ViewModelCharactersFactory

    val viewModelCharacters by viewModels<ViewModelCharacters>(factoryProducer = { factory })

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
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModelCharacters = ViewModelProvider(this)[ViewModelCharacters::class.java]
        observeLiveData()

        recyclerViewCharacters = binding.recyclerviewCharacters
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
            viewModelCharacters.getFilteredCharacters(filter)
            binding.swipeRefreshLayoutCharacters.isRefreshing = false
        }

        binding.clearFilterButtonCharacters.setOnClickListener {
            filter = CharactersFilter("", "", "", "", "")
            viewModelCharacters.getFilteredCharacters(filter)
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

        viewModelCharacters.isEmpty.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "No characters found, check filter or/and connect to network for loading more data", Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeRecyclerView() {
        layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerViewCharacters.layoutManager = layoutManager
        adapterCharacters = AdapterCharacters() { location ->
            val fragmentCharacterDetails = FragmentCharacterDetail.newInstance(location)
            navigator.navigate(fragmentCharacterDetails)
        }
        recyclerViewCharacters.adapter = adapterCharacters
        initializePagination()
    }

    private fun initializePagination() {
        recyclerViewCharacters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    companion object {
        fun newInstance() = FragmentCharacters()
    }
}