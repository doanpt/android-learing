package com.ddona.pokedex.network.retrofit

import com.ddona.pokedex.model.PokemonResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET


interface PokemonService {
    @GET("pokemon")
    fun getAllPokemons(): Observable<PokemonResponse>
}