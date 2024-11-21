package com.koai.netlogger.di

import com.koai.base.main.extension.navigatorViewModel
import com.koai.netlogger.NetLogInterceptor
import com.koai.netlogger.NetLogNavigator
import com.koai.netlogger.repository.INetLogRepository
import com.koai.netlogger.repository.NetLogRepositoryImpl
import org.koin.dsl.module

object NetLogModule {
    fun init() = module {
        single<INetLogRepository> { NetLogRepositoryImpl() }
        single { NetLogInterceptor(get()) }
        navigatorViewModel { NetLogNavigator() }
    }
}