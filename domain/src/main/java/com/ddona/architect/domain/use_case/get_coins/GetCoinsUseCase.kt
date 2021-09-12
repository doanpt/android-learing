package com.ddona.architect.domain.use_case.get_coins

import com.ddona.architect.domain.common.Resource
import com.ddona.architect.domain.model.Coin
import com.ddona.architect.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

// For many use case. create base use case class:
// https://www.toptal.com/android/android-apps-mvvm-with-clean-architecture
// https://github.com/android10/Android-CleanArchitecture/tree/master/domain/src/main/java/com/fernandocejas/android10/sample/domain/interactor
class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading<List<Coin>>())
            val coins = repository.getCoins()
            emit(Resource.Success<List<Coin>>(coins))
        } catch (e: IOException) {
            emit(Resource.Error<List<Coin>>("Couldn't reach server. Check your internet connection."))
        }
    }
}