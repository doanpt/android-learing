package com.ddona.pokedex.network.retrofit

import com.ddona.pokedex.util.Const
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object JsonApiClient {
    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Const.BASE_JSON_API_URL)
            .build()
    }

    val retrofitService: JsonService by lazy {
        retrofit().create(JsonService::class.java)
    }
}