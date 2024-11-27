package com.koai.sample

import com.koai.base.BaseApplication
import com.koai.netlogger.di.NetLogModule
import com.koai.sample.di.AppModule
import org.koin.dsl.module

class MyApplication : BaseApplication() {
    override fun appModule() = module {
        includes(
            super.appModule(),
            NetLogModule.init(),
            AppModule.init()
        )

    }
}