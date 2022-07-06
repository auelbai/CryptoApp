package com.example.cryptoapp.di

import com.example.cryptoapp.data.workers.ChildWorkerFactory
import com.example.cryptoapp.data.workers.CoinWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerModule {

    @IntoMap
    @WorkerKey(CoinWorker::class)
    @Binds
    fun bindCoinWorkerFactory(worker: CoinWorker.Factory): ChildWorkerFactory

}