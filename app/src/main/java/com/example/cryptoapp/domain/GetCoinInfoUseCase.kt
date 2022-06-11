package com.example.cryptoapp.domain

class GetCoinInfoUseCase(private val repository: Repository) {

    operator fun invoke(fromSymbols: String) = repository.getCoinInfo(fromSymbols)
}