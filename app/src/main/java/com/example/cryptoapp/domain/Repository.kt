package com.example.cryptoapp.domain

import androidx.lifecycle.LiveData
import java.text.DateFormatSymbols

interface Repository {

    fun getCoinInfoList(): LiveData<List<CoinInfo>>

    fun getCoinInfo(fromSymbols: String): LiveData<CoinInfo>

    suspend fun loadData()
}