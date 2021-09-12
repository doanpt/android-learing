package com.ddona.architect.domain.repository

import com.ddona.architect.domain.model.Coin
import com.ddona.architect.domain.model.CoinDetail

interface CoinRepository {
    suspend fun getCoins(): List<Coin>

    suspend fun getCoinById(coinId: String): CoinDetail
}