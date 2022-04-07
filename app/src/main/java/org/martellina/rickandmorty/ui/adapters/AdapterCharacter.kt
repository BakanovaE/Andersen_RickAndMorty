package org.martellina.rickandmorty.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.martellina.rickandmorty.databinding.CharacterItemBinding
import org.martellina.rickandmorty.network.model.CharacterInfo

class AdapterCharacter (private val onClickListener: (CharacterInfo) -> Unit)
    : RecyclerView.Adapter<AdapterCharacter.ViewHolderCharacterInEpisode>() {

    private val charactersList = ArrayList<CharacterInfo>()

    inner class ViewHolderCharacterInEpisode(private val binding: CharacterItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: CharacterInfo) {
            with(binding) {
                textViewCharacterName.text = character.name
                textViewCharacterGender.text = character.gender
                textViewCharacterStatus.text = character.status
                textViewCharacterSpecies.text = character.species
                root.setOnClickListener {
                    onClickListener.invoke(charactersList[adapterPosition])
                }
                Picasso.get()
                    .load(character.image)
                    .into(imageViewCharacter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCharacterInEpisode {
        val binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderCharacterInEpisode(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderCharacterInEpisode, position: Int) {
        val item = charactersList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = charactersList.size

    fun updateList(charactersList: List<CharacterInfo>) {
        this.charactersList.clear()
        this.charactersList.addAll(charactersList)
        notifyDataSetChanged()
    }
}