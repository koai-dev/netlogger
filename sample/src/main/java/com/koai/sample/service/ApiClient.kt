package com.koai.sample.service

import com.koai.base.network.BaseApiController
import com.koai.netlogger.NetLogInterceptor

class ApiClient(private val interceptor: NetLogInterceptor): BaseApiController<ApiService>() {
    override fun getApiService() = ApiService::class.java

    override fun getBaseUrl() = "http://ip-api.com/ "

    override fun getNetworkInterceptor() = interceptor
}