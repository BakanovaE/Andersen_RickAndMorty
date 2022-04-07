package org.martellina.rickandmorty.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import org.martellina.rickandmorty.databinding.FragmentFilterEpisodesBinding
import org.martellina.rickandmorty.network.model.Episodes
import org.martellina.rickandmorty.network.model.EpisodesFilter

class FragmentFilterEpisodes: DialogFragment() {

    private lateinit var binding: FragmentFilterEpisodesBinding
    private var filter = EpisodesFilter()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = FragmentFilterEpisodesBinding.inflate(LayoutInflater.from(requireContext()))

        val args = arguments?.getParcelable<EpisodesFilter>(FFE)
        if (args != null) {
            binding.editTextNameFilterEpisodes.setText(args.name)
            binding.editTextCodeFilterEpisodes.setText(args.code)
        }

        val builder = AlertDialog.Builder(requireContext())

        val dialog = builder
            .setView(binding.root)
            .setPositiveButton("Submit") {_, _  ->
                val name = if (binding.editTextNameFilterEpisodes.text.isNotEmpty()) {
                    binding.editTextNameFilterEpisodes.text.toString()
                } else null
                val code = if (binding.editTextCodeFilterEpisodes.text.isNotEmpty()) {
                    binding.editTextCodeFilterEpisodes.text.toString()
                } else null
                filter = EpisodesFilter(name, code)
                (parentFragment as FragmentEpisodes).getFilteredEpisodes(filter)
            }
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Clear filter and exit") {_, _ ->
                (parentFragment as FragmentEpisodes).getFilteredEpisodes(EpisodesFilter("", ""))
            }
            .create()
        return dialog
    }

    companion object {
    private const val FFE = "FFE"
        fun newInstance(filter: EpisodesFilter): FragmentFilterEpisodes {
            val fragment = FragmentFilterEpisodes()
            val args = Bundle().apply {
                putParcelable(FFE, filter)
            }
            fragment.arguments = args
            return fragment
        }
    }
}