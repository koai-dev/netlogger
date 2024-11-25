package com.koai.netlogger

import com.koai.netlogger.model.NetLogItem
import com.koai.netlogger.repository.INetLogRepository
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class NetLogInterceptor(
    private val netLogRepository: INetLogRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestTime = System.nanoTime()
        val response = chain.proceed(request)
        val responseTime = System.nanoTime()

        val url = request.url.toString()
//        val code = response.code.toString()
        val totalResponseTimeInMs = responseTime - requestTime
        val totalResponseTime = TimeUnit.NANOSECONDS.toMillis(totalResponseTimeInMs)

//        val bodyString = response.body.string()

        netLogRepository.addItem(
            NetLogItem(
                request = request,
                response = null,
                responseBody = "",
                requestTime = requestTime,
                totalResponseTime = totalResponseTime,
                url = url,
                code = "200"
            )
        )
        return response
    }
}