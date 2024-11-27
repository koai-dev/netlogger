package com.koai.sample.di

import com.koai.sample.MainViewModel
import com.koai.sample.service.ApiClient
import com.koai.sample.service.ApiService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    fun init() = module {
        //service
        factory<ApiService?> { ApiClient(get()).getService(get()) }
        //viewmodel
        viewModel { MainViewModel(get()) }
    }
}