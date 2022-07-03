package com.example.cryptoapp.data.database

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.workers.CoinWorker
import com.example.cryptoapp.domain.CoinInfo
import com.example.cryptoapp.domain.Repository
import kotlinx.coroutines.delay
import java.lang.Exception

class CoinRepositoryImpl(
    private val application: Application
) : Repository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinInfoDao()
    private val mapper = CoinMapper()

    override fun getCoinInfoList(): LiveData<List<CoinInfo>> {
        return Transformations.map(coinInfoDao.getPriceList()) {
            it.map {
                mapper.mapDbModelToEntity(it)
            }
        }
    }

    override fun getCoinInfo(fromSymbols: String): LiveData<CoinInfo> {
        return Transformations.map(coinInfoDao.getPriceInfoAboutCoin(fromSymbols)) {
            mapper.mapDbModelToEntity(it)
        }
    }

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            CoinWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            CoinWorker.makeRequest()
        )
    }
}