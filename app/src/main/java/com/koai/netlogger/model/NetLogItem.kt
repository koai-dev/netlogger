package com.koai.netlogger.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import okhttp3.Request
import okhttp3.Response

@Parcelize
data class NetLogItem(
    val request: @RawValue Request?,
    val response: @RawValue Response?,
    val responseBody: String,
    val requestTime: Long,
    val totalResponseTime: Long,
    val url: String,
    val code: String,
) : Parcelable
