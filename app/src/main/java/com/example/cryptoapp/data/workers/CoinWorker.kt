package com.example.cryptoapp.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.network.ApiService
import kotlinx.coroutines.delay
import java.lang.Exception

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

    companion object {
        const val NAME = "CoinWorker"

        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<CoinWorker>().build()
        }
    }
}

