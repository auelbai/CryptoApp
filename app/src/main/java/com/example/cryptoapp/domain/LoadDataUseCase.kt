package com.example.cryptoapp.domain

class LoadDataUseCase(private val repository: Repository) {

    suspend operator fun invoke() = repository.loadData()
}