package com.example.cryptoapp.data.mapper

import com.example.cryptoapp.data.database.CoinInfoDbModel
import com.example.cryptoapp.data.network.models.CoinInfoDto
import com.example.cryptoapp.data.network.models.CoinInfoJsonDto
import com.example.cryptoapp.data.network.models.CoinNameListDto
import com.example.cryptoapp.domain.CoinInfo
import com.google.gson.Gson

class CoinMapper {
    fun mapJsonToDto(json: CoinInfoJsonDto): List<CoinInfoDto> {
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = json.json ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    fun mapDtoToDbModel(coinInfoDto: CoinInfoDto): CoinInfoDbModel {
        return CoinInfoDbModel(
            fromSymbol = coinInfoDto.fromSymbol,
            toSymbol = coinInfoDto.toSymbol,
            price = coinInfoDto.price,
            lastUpdate = coinInfoDto.lastUpdate,
            lowDay = coinInfoDto.lowDay,
            highDay = coinInfoDto.highDay,
            lastMarket = coinInfoDto.lastMarket,
            imageUrl = coinInfoDto.imageUrl
        )
    }

    fun mapNameListToString(names: CoinNameListDto): String {
        return names.names?.map {
            it.coinNameContainers?.name
        }?.joinToString(",") ?: ""
    }

    fun mapDbModelToEntity(dbModel: CoinInfoDbModel): CoinInfo {
        return CoinInfo(
            fromSymbol = dbModel.fromSymbol,
            toSymbol = dbModel.toSymbol,
            price = dbModel.price,
            lastUpdate = dbModel.lastUpdate,
            lowDay = dbModel.lowDay,
            highDay = dbModel.highDay,
            lastMarket = dbModel.lastMarket,
            imageUrl = dbModel.imageUrl
        )
    }
}