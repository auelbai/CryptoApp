package com.example.cryptoapp.data.workers

import android.content.Context
import androidx.work.*
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.network.ApiService
import kotlinx.coroutines.delay
import java.lang.Exception
import javax.inject.Inject

class CoinWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val coinInfoDao: CoinInfoDao,
    private val mapper: CoinMapper
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        while (true) {
            try {
                val topCoinsInfo = apiService.getTopCoinsInfo(limit = 50)
                val fSymbol = mapper.mapNameListToString(topCoinsInfo)
                val coinsInfoListDto =
                    mapper.mapJsonToDto(apiService.getFullPriceList(fSyms = fSymbol))
                val dbModelList = coinsInfoListDto.map {
                    mapper.mapDtoToDbModel(it)
                }
                coinInfoDao.insertPriceList(dbModelList)
            } catch (e: Exception) {
            }
            delay(10000)
        }
    }

    class Factory @Inject constructor(
        private val apiService: ApiService,
        private val coinInfoDao: CoinInfoDao,
        private val mapper: CoinMapper
    ) : ChildWorkerFactory {
        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return CoinWorker(context, workerParameters, apiService, coinInfoDao, mapper)
        }

    }

    companion object {
        const val NAME = "CoinWorker"

        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<CoinWorker>().build()
        }
    }
}

