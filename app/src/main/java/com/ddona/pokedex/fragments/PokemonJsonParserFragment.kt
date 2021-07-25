package com.ddona.pokedex.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ddona.pokedex.R
import com.ddona.pokedex.adapter.PokemonAdapter
import com.ddona.pokedex.databinding.FragmentDataBinding
import com.ddona.pokedex.network.json.PokemonParser
import com.ddona.pokedex.util.Const
import kotlinx.coroutines.*

class PokemonJsonParserFragment : Fragment() {
    private val job = Job()

    private val mainScope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var binding: FragmentDataBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataBinding.inflate(inflater)
        mainScope.launch {
            val pokemons =
                PokemonParser.getListPokemon(Const.BASE_URL.plus(Const.POKEMON_LIST_PATH))
            withContext(Dispatchers.Main) {
                binding.rvData.adapter = PokemonAdapter(pokemons)
            }
            Log.d("doanpt", "get ${pokemons.size}")
        }
        return binding.root
    }
}