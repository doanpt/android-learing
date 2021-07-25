package com.ddona.pokedex.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ddona.pokedex.adapter.PokemonAdapter
import com.ddona.pokedex.databinding.FragmentDataBinding
import com.ddona.pokedex.network.retrofit.PokemonClient
import kotlinx.coroutines.*

class PokemonRetrofitFragment : Fragment() {
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
            val pokemons = PokemonClient.retrofitService.getAllPokemons().results
            Log.d("doanpt", "coroutines return ${pokemons.size}")
            withContext(Dispatchers.Main) {
                binding.rvData.adapter = PokemonAdapter(pokemons)
            }
        }
        //refer more sample: https://blog.mindorks.com/using-retrofit-with-kotlin-coroutines-in-android

        return binding.root
    }
}