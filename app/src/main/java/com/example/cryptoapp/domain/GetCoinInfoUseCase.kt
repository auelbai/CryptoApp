package com.example.cryptoapp.domain

import javax.inject.Inject

class GetCoinInfoUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(fromSymbols: String) = repository.getCoinInfo(fromSymbols)
}