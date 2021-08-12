package com.ddona.jetpack.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PassengerClient {

    private const val BASE_URL = "https://api.instantwebtools.net/v1/"

    operator fun invoke(): PassengerApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().also { client ->
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            client.addInterceptor(logging)
        }.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PassengerApi::class.java)
}