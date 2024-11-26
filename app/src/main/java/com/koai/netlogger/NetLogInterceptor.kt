package com.koai.netlogger

import com.koai.netlogger.model.NetLogItem
import com.koai.netlogger.repository.INetLogRepository
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
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
        val code = response.code.toString()
        val totalResponseTimeInMs = responseTime - requestTime
        val totalResponseTime = TimeUnit.NANOSECONDS.toMillis(totalResponseTimeInMs)

        val bodyString = response.body.string()

        val contentType = response.body.contentType()
        val body: ResponseBody = bodyString.toResponseBody(contentType)

        val responseForSaving = response.newBuilder()
            .body(body)
            .request(request)
            .build()

        netLogRepository.addItem(
            NetLogItem(
                request = request,
                response = responseForSaving,
                responseBody = bodyString,
                requestTime = requestTime,
                totalResponseTime = totalResponseTime,
                url = url,
                code = code
            )
        )
        return response.newBuilder().body(body).build()
    }
}