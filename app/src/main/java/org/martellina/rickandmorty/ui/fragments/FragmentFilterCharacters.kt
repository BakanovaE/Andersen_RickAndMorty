package org.martellina.rickandmorty.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import org.martellina.rickandmorty.R
import org.martellina.rickandmorty.databinding.FragmentFilterCharactersBinding
import org.martellina.rickandmorty.network.model.CharactersFilter
import org.martellina.rickandmorty.network.model.EpisodesFilter
import org.martellina.rickandmorty.network.model.LocationsFilter

class FragmentFilterCharacters: DialogFragment() {

    private lateinit var binding: FragmentFilterCharactersBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentFilterCharactersBinding.inflate(LayoutInflater.from(requireContext()))

        val args = arguments?.getParcelable<CharactersFilter>(FRAGMENT_FILTER_CHARACTERS)

        if (args != null) setFilter(args)

        val builder = AlertDialog.Builder(requireContext())

        val dialog = builder
            .setView(binding.root)
            .setPositiveButton("Submit") { _, _, ->
                val name = if (binding.editTextNameFilterCharacters.text.isNotEmpty()) {
                    binding.editTextNameFilterCharacters.text.toString()
                } else ""
                val status = if (binding.spinnerStatusFilterCharacters.selectedItem != "Any") {
                    binding.spinnerStatusFilterCharacters.selectedItem.toString()
                } else ""
                val species = if (binding.editTextSpeciesFilterCharacters.text.isNotEmpty()) {
                    binding.editTextSpeciesFilterCharacters.text.toString()
                } else ""
                val type = if (binding.editTextTypeFilterCharacters.text.isNotEmpty()) {
                    binding.editTextTypeFilterCharacters.text.toString()
                } else ""
                val gender = if (binding.spinnerGenderFilterCharacters.selectedItem != "Any") {
                    binding.spinnerGenderFilterCharacters.selectedItem as String
                } else ""
                val filter = CharactersFilter(name, status, species, type, gender)
                if (isEmpty(filter)) {
                    Toast.makeText(requireContext(), R.string.toast_empty_filter, Toast.LENGTH_SHORT).show()
                }
                if (isSame(args, filter)) {
                    Toast.makeText(requireContext(), R.string.toast_same_filter, Toast.LENGTH_SHORT).show()
                }
                (parentFragment as FragmentCharacters).getFilteredCharacters(filter)
            }
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Clear filter and exit") {_, _ ->
                (parentFragment as FragmentCharacters).getFilteredCharacters(CharactersFilter("", "", "", "", ""))
            }
            .create()
        return dialog
    }

    private fun setFilter(args: CharactersFilter) {
        with(binding) {
            editTextNameFilterCharacters.setText(args.name)
            spinnerStatusFilterCharacters.setSelection(
                resources.getStringArray(R.array.status).indexOf(args.status)
            )
            editTextSpeciesFilterCharacters.setText(args.species)
            editTextTypeFilterCharacters.setText(args.type)
            spinnerGenderFilterCharacters.setSelection(
                resources.getStringArray(R.array.gender).indexOf(args.gender)
            )
            if (!args.status.isNullOrEmpty()) {
                spinnerStatusFilterCharacters.setSelection(
                    resources.getStringArray(R.array.status).indexOf(args.status)
                )
            } else spinnerStatusFilterCharacters.setSelection(0)
            if (!args.gender.isNullOrEmpty()) {
                spinnerGenderFilterCharacters.setSelection(
                    resources.getStringArray(R.array.gender).indexOf(args.gender)
                )
            } else spinnerGenderFilterCharacters.setSelection(0)
        }
    }

    private fun isEmpty(filter: CharactersFilter): Boolean {
        return filter.name == ""
                && filter.status == ""
                && filter.species == ""
                && filter.type == ""
                && filter.gender == ""
    }

    private fun isSame(args: CharactersFilter?, filter: CharactersFilter): Boolean {
        return args?.name == filter.name
                && args?.status == filter.status
                && args?.species == filter.species
                && args?.type == filter.type
                && args?.gender == filter.gender
    }

    companion object {
        private const val FRAGMENT_FILTER_CHARACTERS = "FRAGMENT_FILTER_CHARACTERS"
        fun newInstance(filter: CharactersFilter): FragmentFilterCharacters {
            val fragment = FragmentFilterCharacters()
            val args = Bundle().apply {
                putParcelable(FRAGMENT_FILTER_CHARACTERS, filter)
            }
            fragment.arguments = args
            return fragment
        }
    }
}