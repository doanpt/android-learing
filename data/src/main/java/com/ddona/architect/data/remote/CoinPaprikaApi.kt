package com.ddona.architect.data.remote

import com.ddona.architect.data.entity.CoinDetailEntity
import com.ddona.architect.data.entity.CoinEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinPaprikaApi {
    @GET("/v1/coins")
    suspend fun getCoins(): List<CoinEntity>

    @GET("/v1/coins/{coinId}")
    suspend fun getCoinById(@Path("coinId") coinId: String): CoinDetailEntity
}