package com.ddona.architect.domain.repository

import com.ddona.architect.data.remote.dto.CoinDetailDto
import com.ddona.architect.data.remote.dto.CoinDto

interface CoinRepository {
    suspend fun getCoins(): List<CoinDto>

    suspend fun getCoinById(coinId: String): CoinDetailDto
}