package org.martellina.rickandmorty.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.martellina.rickandmorty.databinding.EpisodeItemBinding
import org.martellina.rickandmorty.network.model.EpisodeInfo

class AdapterEpisode (private val onClickListener: (episode: EpisodeInfo) -> Unit)
    : RecyclerView.Adapter<AdapterEpisode.ViewHolderEpisodeInCharacter>() {

    private val episodesList = ArrayList<EpisodeInfo>()

    inner class ViewHolderEpisodeInCharacter(private val binding: EpisodeItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: EpisodeInfo) {
            with(binding) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEpisodeInCharacter {
        val binding = EpisodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderEpisodeInCharacter(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderEpisodeInCharacter, position: Int) {
        val item = episodesList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = episodesList.size

    fun updateList(episodesList: ArrayList<EpisodeInfo>) {
        this.episodesList.clear()
        this.episodesList.addAll(episodesList)
        notifyDataSetChanged()
    }
}