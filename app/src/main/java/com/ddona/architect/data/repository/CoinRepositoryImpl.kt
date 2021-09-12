package com.ddona.architect.data.repository

import com.ddona.architect.data.remote.CoinPaprikaApi
import com.ddona.architect.data.remote.dto.CoinDetailDto
import com.ddona.architect.data.remote.dto.CoinDto
import com.ddona.architect.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinPaprikaApi
) : CoinRepository {
    override suspend fun getCoins(): List<CoinDto> {
        return api.getCoins()
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        return api.getCoinById(coinId)
    }
}