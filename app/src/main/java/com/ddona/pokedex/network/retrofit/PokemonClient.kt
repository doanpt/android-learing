package com.ddona.pokedex.network.retrofit

import com.ddona.pokedex.util.Const
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//class PokemonClient {
//    companion object {
//        private var instance: PokemonService? = null
//
//        @Synchronized
//        fun getInstance(): PokemonService {
//            if (instance == null)
//                instance = Retrofit.Builder()
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .baseUrl(Const.BASE_URL)
//                    .build()
//                    .create(PokemonService::class.java)
//            return instance as PokemonService
//        }
//    }
//}

object PokemonClient {

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Const.BASE_URL)
            .build()
    }

    val retrofitService: PokemonService by lazy {
        retrofit().create(PokemonService::class.java)
    }
}