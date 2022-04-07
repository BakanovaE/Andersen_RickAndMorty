package org.martellina.rickandmorty.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.martellina.rickandmorty.databinding.LocationItemBinding
import org.martellina.rickandmorty.network.model.LocationInfo

class AdapterLocations(private val onClickListener: (LocationInfo) -> Unit)
    : RecyclerView.Adapter<AdapterLocations.ViewHolderLocations>() {

    private val locationsList = ArrayList<LocationInfo>()

    inner class ViewHolderLocations(private val binding: LocationItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: LocationInfo) {
            with(binding) {
                textViewLocationName.text = location.name
                textViewLocationName.isSelected = true
                textViewLocationType.text = location.type.ifEmpty { "unknown" }
                textViewLocationDimension.text = location.dimension.ifEmpty { "unknown" }
                root.setOnClickListener {
                    onClickListener.invoke(locationsList[adapterPosition])
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderLocations {
        val binding = LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ViewHolderLocations(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderLocations, position: Int) {
        val item = locationsList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = locationsList.size

    fun updateList(locationsList: List<LocationInfo>) {
        this.locationsList.clear()
        this.locationsList.addAll(locationsList)
        notifyDataSetChanged()
    }

}