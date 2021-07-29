package com.ddona.pokedex.network.retrofit

import com.ddona.pokedex.util.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object JsonApiClient {
    private fun retrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(Const.BASE_JSON_API_URL)
            .build()
    }

    val retrofitService: JsonService by lazy {
        retrofit().create(JsonService::class.java)
    }
}