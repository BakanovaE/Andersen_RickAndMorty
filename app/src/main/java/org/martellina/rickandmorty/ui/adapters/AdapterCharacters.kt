package org.martellina.rickandmorty.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.martellina.rickandmorty.databinding.CharacterItemBinding
import org.martellina.rickandmorty.network.model.CharacterInfo

class AdapterCharacters(private val onClickListener: (character: CharacterInfo) -> Unit)
    : RecyclerView.Adapter<AdapterCharacters.ViewHolderCharacters>() {

    private val charactersList = ArrayList<CharacterInfo>()

    inner class ViewHolderCharacters(private val binding: CharacterItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: CharacterInfo) {
            with(binding) {
                textViewCharacterName.text = character.name
                textViewCharacterGender.text = character.gender
                textViewCharacterStatus.text = character.status
                textViewCharacterSpecies.text = character.species
                root.setOnClickListener {
                    onClickListener(character)
                }
                Picasso.get()
                    .load(character.image)
                    .into(imageViewCharacter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCharacters {
        val binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderCharacters(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderCharacters, position: Int) {
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