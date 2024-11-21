package com.koai.netlogger

import com.koai.netlogger.model.NetLogItem
import com.koai.netlogger.repository.INetLogRepository
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
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
        callLog(request, response, requestTime, responseTime)
        return response
    }

    private fun callLog(
        request: Request,
        response: Response,
        requestTime: Long,
        responseTime: Long
    ) {
        val url = request.url.toString()
        val code = response.code.toString()
        val totalResponseTimeInMs = responseTime - requestTime
        val totalResponseTime = TimeUnit.NANOSECONDS.toMillis(totalResponseTimeInMs)

        val contentType: MediaType? = response.body.contentType()
        val bodyString = response.body.string()
        val body = bodyString.toResponseBody(contentType)

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
    }
}