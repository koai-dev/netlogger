package com.koai.netlogger.utils

import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
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
import java.util.regex.Pattern

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
    var formattedBody =
        try {
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

fun Request.getBodyString() =
    try {
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
    var formattedBody =
        try {
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

fun RequestBody.requestBodyToJson(): String {
    // Convert RequestBody to string (assuming it's a JSON)
    val buffer = Buffer()
    this.writeTo(buffer)
    val jsonString = buffer.readUtf8()
    return jsonString
}

fun Context.copyToClipboard(
    text: String,
    onDone: (() -> Unit)? = null,
) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = android.content.ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
    onDone?.invoke()
}

private var urlPattern: Pattern =
    Pattern.compile(
        (
            "^((https|http|ftp|rtsp|mms)?://)" +
                "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" + // ftp的user@
                "(([0-9]{1,3}\\.){3}[0-9]{1,3}" + // IP形式的URL- 199.194.52.184
                "|" + // 允许IP和DOMAIN（域名）
                "([0-9a-z_!~*'()-]+\\.)*" + // 域名- www.
                "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." + // 二级域名
                "[a-z]{2,6})" + // first level domain- .com or .museum
                "(:[0-9]{1,4})?" + // 端口- :80
                "((/?)|" + // a slash isn't required if there is no file name
                "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$"
        ),
    )

/**
 * 判断字符串是否是url
 *
 * @param string
 * @return
 */
fun String?.isUrl(): Boolean {
    return this?.let { urlPattern.matcher(it).matches() } ?: false
}

/**
 * json 格式化缩进(格式化前不能有缩进，最好是格式化从服务端下发的)
 *
 * @param jsonStr
 * @return
 */
fun String?.jsonFormat(): String {
    if (this == null) return ""
    var level = 0
    val builder = java.lang.StringBuilder()
    for (element in this) {
        val c = element
        if (level > 0 && '\n' == builder[builder.length - 1]) {
            builder.append(level.getHierarchyStr())
        }
        when (c) {
            '{', '[' -> {
                builder.append(c).append("\n")
                level++
            }

            ',' -> builder.append(c).append("\n")
            '}', ']' -> {
                builder.append("\n")
                level--
                builder.append(level.getHierarchyStr())
                builder.append(c)
            }

            else -> builder.append(c)
        }
    }

    return builder.toString()
}

/**
 * 对应层级前面所需的空格数
 *
 * @param hierarchy 缩进层级
 * @return
 */
fun Int.getHierarchyStr(): String {
    val levelStr = java.lang.StringBuilder()
    for (levelI in 0 until this) {
        levelStr.append("      ")
    }
    return levelStr.toString()
}
