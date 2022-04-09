package org.martellina.rickandmorty.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import org.martellina.rickandmorty.R
import org.martellina.rickandmorty.databinding.FragmentFilterLocationsBinding
import org.martellina.rickandmorty.network.model.EpisodesFilter
import org.martellina.rickandmorty.network.model.LocationsFilter

class FragmentFilterLocations: DialogFragment() {

    private lateinit var binding: FragmentFilterLocationsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentFilterLocationsBinding.inflate(LayoutInflater.from(requireContext()))

        val args = arguments?.getParcelable<LocationsFilter>(FFL)
        if (args != null) {
            binding.editTextNameFilterLocations.setText(args.name)
            binding.editTextTypeFilterLocations.setText(args.type)
            binding.editTextDimensionFilterLocations.setText(args.dimension)
        }

        val builder = AlertDialog.Builder(requireContext())

        val dialog = builder
            .setView(binding.root)
            .setPositiveButton("Submit") {_, _  ->
                val name = if (binding.editTextNameFilterLocations.text.isNotEmpty()) {
                    binding.editTextNameFilterLocations.text.toString()
                } else ""
                val type = if (binding.editTextTypeFilterLocations.text.isNotEmpty()) {
                    binding.editTextTypeFilterLocations.text.toString()
                } else ""
                val dimension = if (binding.editTextDimensionFilterLocations.text.isNotEmpty()) {
                    binding.editTextDimensionFilterLocations.text.toString()
                } else ""
                val filter = LocationsFilter(name, type, dimension)
                if (isEmpty(filter)) {
                    Toast.makeText(requireContext(), R.string.toast_empty_filter, Toast.LENGTH_SHORT).show()
                }
                if (isSame(args, filter)) {
                    Toast.makeText(requireContext(), R.string.toast_same_filter, Toast.LENGTH_SHORT).show()
                }
                (parentFragment as FragmentLocations).getFilteredLocations(filter)
            }
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Clear filter and exit") {_, _ ->
                (parentFragment as FragmentLocations).getFilteredLocations(LocationsFilter("", "", ""))
            }
            .create()
        return dialog
    }

    private fun isEmpty(filter: LocationsFilter): Boolean {
        return filter.name == "" && filter.type == "" && filter.dimension == ""
    }

    private fun isSame(args: LocationsFilter?, filter: LocationsFilter): Boolean {
        return args?.name == filter.name
                && args?.type == filter.type
                && args?.dimension == filter.dimension
    }

    companion object {
        private const val FFL ="FFL"
        fun newInstance(filter: LocationsFilter): FragmentFilterLocations{
            val fragment = FragmentFilterLocations()
            val args = Bundle().apply {
                putParcelable(FFL, filter)
            }
            fragment.arguments = args
            return fragment
        }
    }
}