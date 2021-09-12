package com.ddona.architect.data.repository

import com.ddona.architect.data.entity.CoinEntity
import com.ddona.architect.data.entity.toCoin
import com.ddona.architect.data.entity.toCoinDetail
import com.ddona.architect.data.remote.CoinPaprikaApi
import com.ddona.architect.domain.model.Coin
import com.ddona.architect.domain.model.CoinDetail
import com.ddona.architect.domain.repository.CoinRepository
import javax.inject.Inject

//For other way to convert data from domain to data and from data to domain:
//https://github.com/canhtv-0838/CleanArchitectureExample/blob/master/data/src/main/java/com/canh/data/NoteModelMapperImpl.kt
class CoinRepositoryImpl @Inject constructor(
    private val api: CoinPaprikaApi
) : CoinRepository {
    override suspend fun getCoins(): List<Coin> {
        return api.getCoins().map { it.toCoin() }
    }

    override suspend fun getCoinById(coinId: String): CoinDetail {
        return api.getCoinById(coinId).toCoinDetail()
    }
}