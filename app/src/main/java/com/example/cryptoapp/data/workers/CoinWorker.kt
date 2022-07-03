package com.example.cryptoapp.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory
import kotlinx.coroutines.delay
import java.lang.Exception

class CoinWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val apiService = ApiFactory.apiService
    private val coinInfoDao = AppDatabase.getInstance(context).coinInfoDao()
    private val mapper = CoinMapper()

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

        fun makeRequest() : OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<CoinWorker>().build()
        }
    }
}

