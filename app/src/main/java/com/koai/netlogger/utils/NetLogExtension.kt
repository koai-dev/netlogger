package com.koai.netlogger.utils

import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import com.google.gson.Gson
import com.koai.base.utils.GsonUtils
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import java.text.SimpleDateFormat
import java.util.*


fun Date.format(mask: String): String {
    val simpleDateFormat = SimpleDateFormat(mask, Locale.getDefault())
    return simpleDateFormat.format(this)
}

fun Request.formatToString(): String {
    val stringBuilder = StringBuilder()

    val headerMap = this.headers.toMultimap()

    stringBuilder.append("-- Headers --")
    stringBuilder.append("\n")
    stringBuilder.append("\n")
    for ((key, value) in headerMap) {
        stringBuilder.append(key)
        stringBuilder.append("\n")
        stringBuilder.append(value.toString())
        stringBuilder.append("\n")
        stringBuilder.append("\n")
    }

    stringBuilder.append("-- Body --")
    stringBuilder.append("\n")
    stringBuilder.append("\n")

    val bodyString = getBodyString()
    var formattedBody = try {
        if (bodyString.isNotEmpty()) JSONObject(bodyString).toString(4) else bodyString
    } catch (e: Exception) {
        try {
            if (bodyString.isNotEmpty()) JSONArray(bodyString).toString(4) else bodyString
        } catch (e: Exception) {
            bodyString.replace("\\", "")
        }
    }
    formattedBody = formattedBody.replace("\\", "")
    stringBuilder.append(formattedBody)
    stringBuilder.append("\n")
    stringBuilder.append("\n")
    stringBuilder.append("\n")
    stringBuilder.append("\n")

    return stringBuilder.toString()
}

fun Request.getBodyString() = try {
    val requestCopy = this.newBuilder().build()
    val buffer = Buffer()
    val requestBody = requestCopy.body
    requestBody?.writeTo(buffer)

    val contentType = requestBody?.contentType()
    val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8
    buffer.readString(charset)
} catch (e: Exception) {
    ""
}

fun Response.formatToString(body: String): String {
    val stringBuilder = StringBuilder()

    val headerMap = this.headers.toMultimap()

    stringBuilder.append("-- Headers --")
    stringBuilder.append("\n")
    stringBuilder.append("\n")
    for ((key, value) in headerMap) {
        stringBuilder.append(key)
        stringBuilder.append("\n")
        stringBuilder.append(value.toString())
        stringBuilder.append("\n")
        stringBuilder.append("\n")
    }
    var formattedBody = try {
        if (body.isNotEmpty()) JSONObject(body).toString(4) else body
    } catch (e: Exception) {
        try {
            if (body.isNotEmpty()) JSONArray(body).toString(4) else body
        } catch (e: Exception) {
            ""
        }
    }
    formattedBody = formattedBody.replace("\\", "")
    stringBuilder.append("-- Body --")
    stringBuilder.append("\n")
    stringBuilder.append("\n")
    stringBuilder.append(formattedBody)
    stringBuilder.append("\n")
    stringBuilder.append("\n")
    stringBuilder.append("\n")
    stringBuilder.append("\n")

    return stringBuilder.toString()
}

fun <T : Parcelable> Bundle.getSafeParcelable(
    name: String,
    clazz: Class<T>,
): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelable(name, clazz)
    } else {
        this.getParcelable(name)
    }
}

fun RequestBody.requestBodyToJson(): String? {
    // Convert RequestBody to string (assuming it's a JSON)
    val buffer = okio.Buffer()
    this.writeTo(buffer)
    val jsonString = buffer.readUtf8()

    // Optionally, you can parse it into a JSON object (if needed)
    val jsonObject = Gson().fromJson(jsonString, Any::class.java)
    return GsonUtils.toJson(jsonObject) // Convert back to JSON string
}

fun Context.copyToClipboard(text: String, onDone: (() -> Unit)? = null) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = android.content.ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
    onDone?.invoke()
}