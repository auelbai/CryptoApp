package com.example.cryptoapp.data.database

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.domain.CoinInfo
import com.example.cryptoapp.domain.Repository
import kotlinx.coroutines.delay
import java.lang.Exception

class CoinRepositoryImpl(application: Application) : Repository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinInfoDao()
    private val mapper = CoinMapper()
    private val apiService = ApiFactory.apiService

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

    override suspend  fun loadData() {
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
            } catch (e: Exception){
            }
            delay(10000)
        }
    }
}