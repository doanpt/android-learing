package com.ddona.pokedex.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddona.pokedex.databinding.ItemPokemonBinding
import com.ddona.pokedex.model.Pokemon


class PokemonAdapter(private val mPokemons: List<Pokemon>) :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //WITHOUT BINDING
        //val view: View = LayoutInflater.from(parent.context)
        //  .inflate(R.layout.item_pokemon, parent, false)
        //return ViewHolder(view)
        val binding: ItemPokemonBinding = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mPokemons[position])
        //WITHOUT BINDING
        //holder.tvName.text = mPokemons!![position].name
        //Glide.with(holder.imgAvatar).load(mPokemons[position].url)
        //  .into(holder.imgAvatar)
    }

    override fun getItemCount(): Int {
        return mPokemons.size
    }

    class ViewHolder(private var binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //WITHOUT BINDING
        //var imgAvatar: ImageView = itemView.findViewById(R.id.img_avatar)
        //var tvName: TextView = itemView.findViewById(R.id.tv_name)

        fun bind(pokemon: Pokemon) {
            binding.pokemon = pokemon
        }
    }
}