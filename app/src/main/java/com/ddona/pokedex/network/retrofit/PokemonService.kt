package com.ddona.pokedex.network.retrofit

import com.ddona.pokedex.model.PokemonResponse
import retrofit2.http.GET


interface PokemonService {
    @GET("pokemon")
    suspend fun getAllPokemons(): PokemonResponse
}