package com.ddona.architect.domain.repository

import com.ddona.architect.domain.model.Coin
import com.ddona.architect.domain.model.CoinDetail

//For other way to convert data from domain to data and from data to domain:
//https://github.com/canhtv-0838/CleanArchitectureExample/blob/master/domain/src/main/java/com/canh/domain/repository/NoteModelMapper.kt
interface CoinRepository {
    suspend fun getCoins(): List<Coin>

    suspend fun getCoinById(coinId: String): CoinDetail
}