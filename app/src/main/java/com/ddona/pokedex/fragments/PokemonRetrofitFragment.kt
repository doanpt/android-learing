package com.ddona.pokedex.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ddona.pokedex.adapter.PokemonAdapter
import com.ddona.pokedex.databinding.FragmentDataBinding
import com.ddona.pokedex.model.PokemonResponse
import com.ddona.pokedex.network.retrofit.PokemonClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonRetrofitFragment : Fragment() {

    private lateinit var binding: FragmentDataBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataBinding.inflate(inflater)
        PokemonClient.retrofitService.getAllPokemons()
            .enqueue(object : Callback<PokemonResponse> {
                override fun onResponse(
                    call: Call<PokemonResponse>,
                    response: Response<PokemonResponse>
                ) {
                    if (response.isSuccessful) {
                        val pokemons = response.body()
                        binding.rvData.adapter = PokemonAdapter(pokemons!!.results)
                        Log.d("doanpt", "get ${pokemons.results.size}")
                    } else {
                        Toast.makeText(context, "There are no Pokemon", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                    Log.e("doanpt", "call error ${t.message}")
                    t.printStackTrace()
                }

            })


        return binding.root
    }
}