package org.martellina.rickandmorty.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.martellina.rickandmorty.databinding.EpisodeItemBinding
import org.martellina.rickandmorty.network.model.EpisodeInfo

class AdapterEpisodes(private val onClickListener: (episode: EpisodeInfo) -> Unit) :
    RecyclerView.Adapter<AdapterEpisodes.EpisodesViewHolder>() {

    private var episodesList = ArrayList<EpisodeInfo>()

    inner class EpisodesViewHolder(val binding: EpisodeItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(episode: EpisodeInfo){
            with(binding){
                textViewEpisodeName.text = episode.name
                textViewEpisodeName.isSelected = true
                textViewEpisodeNumber.text = episode.episode
                textViewEpisodeAirDate.text = episode.air_date
                root.setOnClickListener {
                    onClickListener(episode)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterEpisodes.EpisodesViewHolder {
        val binding = EpisodeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EpisodesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val item = episodesList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return episodesList.size
    }

    fun updateList(episodesList: List<EpisodeInfo>) {
        this.episodesList.clear()
        this.episodesList.addAll(episodesList)
        notifyDataSetChanged()
    }
}