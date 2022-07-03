package com.example.cryptoapp.di

import android.app.Application
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.network.ApiService
import com.example.cryptoapp.data.repository.CoinRepositoryImpl
import com.example.cryptoapp.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    fun bindsRepository(impl: CoinRepositoryImpl): Repository

    companion object {

        @Provides
        @ApplicationScope
        fun provideCoinInfoDao(
            application: Application
        ): CoinInfoDao {
            return AppDatabase.getInstance(application).coinInfoDao()
        }

        @Provides
        @ApplicationScope
        fun provideApiService() : ApiService {
            return ApiFactory.apiService
        }
    }
}