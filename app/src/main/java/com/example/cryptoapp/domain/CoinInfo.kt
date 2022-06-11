package com.example.cryptoapp.domain


data class CoinInfo(
    val fromSymbol: String,
    val toSymbol: String?,
    val price: String?,
    val lastUpdate: Long?,
    val openDay: String?,
    val highDay: String?,
    val lastMarket: String?,
    val imageUrl: String?
)