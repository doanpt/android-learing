package com.ddona.pokedex.network.retrofit

import com.ddona.pokedex.util.Const
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object JsonApiClient {
    private fun retrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val headerInterceptor = Interceptor {
            val originalRequest: Request = it.request()
            val newRequest: Request = originalRequest.newBuilder()
                .header("Interceptor-Header", "headerInterceptor")
                .build()
            it.proceed(newRequest)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
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