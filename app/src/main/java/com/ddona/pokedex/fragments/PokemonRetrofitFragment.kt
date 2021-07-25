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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class PokemonRetrofitFragment : Fragment() {

    private lateinit var binding: FragmentDataBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataBinding.inflate(inflater)

        PokemonClient.retrofitService.getAllPokemons()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                Log.d("doanpt", "retrofit get ${it.results.size}")
                binding.rvData.adapter = PokemonAdapter(it.results)
            }

        return binding.root
    }
}