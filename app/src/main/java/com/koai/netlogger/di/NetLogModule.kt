package com.koai.netlogger.di

import com.koai.base.main.extension.navigatorViewModel
import com.koai.base.main.extension.screenViewModel
import com.koai.netlogger.NetLogInterceptor
import com.koai.netlogger.NetLogNavigator
import com.koai.netlogger.repository.INetLogRepository
import com.koai.netlogger.repository.NetLogRepositoryImpl
import com.koai.netlogger.ui.list.ListLogViewModel
import org.koin.dsl.module

object NetLogModule {
    fun init() = module {
        single<INetLogRepository> { NetLogRepositoryImpl() }
        single { NetLogInterceptor(get()) }
        navigatorViewModel { NetLogNavigator() }
        screenViewModel { ListLogViewModel(get()) }
    }
}