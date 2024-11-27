package com.koai.netlogger.ui.jsonviewer.adapter

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koai.netlogger.ui.jsonviewer.view.JsonItemView
import com.koai.netlogger.utils.getHierarchyStr
import com.koai.netlogger.utils.isUrl
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

/**
 * Created by yuyuhang on 2017/11/29.
 */
class JsonViewerAdapter : BaseJsonViewerAdapter<JsonViewerAdapter.JsonItemViewHolder?> {
    private var jsonStr: String? = null

    private var mJSONObject: JSONObject? = null
    private var mJSONArray: JSONArray? = null

    private var clicked = false

    constructor(jsonStr: String?) {
        this.jsonStr = jsonStr

        var `object`: Any? = null
        try {
            `object` = JSONTokener(jsonStr).nextValue()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (`object` != null && `object` is JSONObject) {
            mJSONObject = `object`
        } else if (`object` != null && `object` is JSONArray) {
            mJSONArray = `object`
        } else {
            throw IllegalArgumentException("jsonStr is illegal.")
        }
    }

    constructor(jsonObject: JSONObject?) {
        this.mJSONObject = jsonObject
        requireNotNull(mJSONObject) { "jsonObject can not be null." }
    }

    constructor(jsonArray: JSONArray?) {
        this.mJSONArray = jsonArray
        requireNotNull(mJSONArray) { "jsonArray can not be null." }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): JsonItemViewHolder {
        return JsonItemViewHolder(JsonItemView(parent.context))
    }

    override fun onBindViewHolder(
        holder: JsonItemViewHolder,
        position: Int,
    ) {
        val itemView: JsonItemView? = holder.jsonItemView
        itemView ?: return
        itemView.setTextSize(TEXT_SIZE_DP)
        itemView.setRightColor(BRACES_COLOR)
        if (mJSONObject != null) {
            if (position == 0) {
                itemView.hideLeft()
                itemView.hideIcon()
                itemView.showRight("{")
                return
            } else if (position == itemCount - 1) {
                itemView.hideLeft()
                itemView.hideIcon()
                itemView.showRight("}")
                return
            } else if (mJSONObject?.names() == null) {
                return
            }

            val key = mJSONObject?.names()?.optString(position - 1) // 遍历key
            val value = mJSONObject?.opt(key)
            if (position < itemCount - 2) {
                if (key != null) {
                    if (value != null) {
                        handleJsonObject(key, value, itemView, true, 1)
                    }
                }
            } else {
                if (key != null) {
                    if (value != null) {
                        handleJsonObject(key, value, itemView, false, 1)
                    }
                } // 最后一组，结尾不需要逗号
            }
        }

        if (mJSONArray != null) {
            if (position == 0) {
                itemView.hideLeft()
                itemView.hideIcon()
                itemView.showRight("[")
                return
            } else if (position == itemCount - 1) {
                itemView.hideLeft()
                itemView.hideIcon()
                itemView.showRight("]")
                return
            }

            val value = mJSONArray?.opt(position - 1) // 遍历array
            if (position < itemCount - 2) {
                if (value != null) {
                    handleJsonArray(value, itemView, true, 1)
                }
            } else {
                if (value != null) {
                    handleJsonArray(value, itemView, false, 1)
                } // 最后一组，结尾不需要逗号
            }
        }
    }

    override fun getItemCount(): Int {
        if (mJSONObject != null) {
            return if (mJSONObject?.names() != null) {
                (mJSONObject?.names()?.length() ?: 0) + 2
            } else {
                2
            }
        }
        if (mJSONArray != null) {
            return (mJSONArray?.length() ?: 0) + 2
        }
        return 0
    }

    /**
     * 处理 value 上级为 JsonObject 的情况，value有key
     *
     * @param value
     * @param key
     * @param itemView
     * @param appendComma
     * @param hierarchy
     */
    private fun handleJsonObject(
        key: String,
        value: Any,
        itemView: JsonItemView,
        appendComma: Boolean,
        hierarchy: Int,
    ) {
        val keyBuilder = SpannableStringBuilder(hierarchy.getHierarchyStr())
        keyBuilder.append("\"").append(key).append("\"").append(":")
        keyBuilder.setSpan(
            ForegroundColorSpan(KEY_COLOR),
            0,
            keyBuilder.length - 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
        keyBuilder.setSpan(
            ForegroundColorSpan(BRACES_COLOR),
            keyBuilder.length - 1,
            keyBuilder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
        )

        itemView.showLeft(keyBuilder)

        handleValue(value, itemView, appendComma, hierarchy)
    }

    /**
     * 处理 value 上级为 JsonArray 的情况，value无key
     *
     * @param value
     * @param itemView
     * @param appendComma 结尾是否需要逗号(最后一组 value 不需要逗号)
     * @param hierarchy   缩进层级
     */
    private fun handleJsonArray(
        value: Any,
        itemView: JsonItemView,
        appendComma: Boolean,
        hierarchy: Int,
    ) {
        itemView.showLeft(SpannableStringBuilder(hierarchy.getHierarchyStr()))

        handleValue(value, itemView, appendComma, hierarchy)
    }

    /**
     * @param value
     * @param itemView
     * @param appendComma 结尾是否需要逗号(最后一组 key:value 不需要逗号)
     * @param hierarchy   缩进层级
     */
    private fun handleValue(
        value: Any?,
        itemView: JsonItemView,
        appendComma: Boolean,
        hierarchy: Int,
    ) {
        val valueBuilder = SpannableStringBuilder()
        if (value is Number) {
            valueBuilder.append(value.toString())
            valueBuilder.setSpan(
                ForegroundColorSpan(NUMBER_COLOR),
                0,
                valueBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        } else if (value is Boolean) {
            valueBuilder.append(value.toString())
            valueBuilder.setSpan(
                ForegroundColorSpan(BOOLEAN_COLOR),
                0,
                valueBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        } else if (value is JSONObject) {
            itemView.showIcon(true)
            valueBuilder.append("Object{...}")
            valueBuilder.setSpan(
                ForegroundColorSpan(BRACES_COLOR),
                0,
                valueBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            itemView.setIconClickListener(
                JsonItemClickListener(
                    value,
                    itemView,
                    appendComma,
                    hierarchy + 1,
                ),
            )
            if (clicked) {
                itemView.clickIcon()
            }
        } else if (value is JSONArray) {
            itemView.showIcon(true)
            valueBuilder.append("Array[").append(value.length().toString()).append("]")
            val len = valueBuilder.length
            valueBuilder.setSpan(
                ForegroundColorSpan(BRACES_COLOR),
                0,
                6,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            valueBuilder.setSpan(
                ForegroundColorSpan(NUMBER_COLOR),
                6,
                len - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            valueBuilder.setSpan(
                ForegroundColorSpan(BRACES_COLOR),
                len - 1,
                len,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            itemView.setIconClickListener(
                JsonItemClickListener(
                    value,
                    itemView,
                    appendComma,
                    hierarchy + 1,
                ),
            )
            if (clicked) {
                itemView.clickIcon()
            }
        } else if (value is String) {
            itemView.hideIcon()
            valueBuilder.append("\"").append(value.toString()).append("\"")
            if (value.toString().isUrl()) {
                valueBuilder.setSpan(
                    ForegroundColorSpan(TEXT_COLOR),
                    0,
                    1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
                valueBuilder.setSpan(
                    ForegroundColorSpan(URL_COLOR),
                    1,
                    valueBuilder.length - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
                valueBuilder.setSpan(
                    ForegroundColorSpan(TEXT_COLOR),
                    valueBuilder.length - 1,
                    valueBuilder.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
            } else {
                valueBuilder.setSpan(
                    ForegroundColorSpan(TEXT_COLOR),
                    0,
                    valueBuilder.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
            }
        } else if (valueBuilder.isEmpty() || value == null) {
            itemView.hideIcon()
            valueBuilder.append("null")
            valueBuilder.setSpan(
                ForegroundColorSpan(NULL_COLOR),
                0,
                valueBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
        if (appendComma) {
            valueBuilder.append(",")
        }

        itemView.showRight(valueBuilder)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun expandAll() {
        clicked = true
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun collapseAll() {
        clicked = false
        notifyDataSetChanged()
    }

    internal inner class JsonItemClickListener(
        private val value: Any,
        private val itemView: JsonItemView,
        private val appendComma: Boolean,
        private val hierarchy: Int,
    ) :
        View.OnClickListener {
        private var isCollapsed = true
        private val isJsonArray = value != null && value is JSONArray

        override fun onClick(view: View) {
            if (itemView.childCount === 1) { // 初始（折叠） --> 展开""
                isCollapsed = false
                itemView.showIcon(false)
                itemView.tag = itemView.rightText
                itemView.showRight(if (isJsonArray) "[" else "{")
                val array = if (isJsonArray) value as JSONArray else (value as JSONObject).names()
                var i = 0
                while (array != null && i < array.length()) {
                    val childItemView: JsonItemView = JsonItemView(itemView.context)
                    childItemView.setTextSize(TEXT_SIZE_DP)
                    childItemView.setRightColor(BRACES_COLOR)
                    val childValue = array.opt(i)
                    if (isJsonArray) {
                        handleJsonArray(
                            childValue,
                            childItemView,
                            i < array.length() - 1,
                            hierarchy,
                        )
                    } else {
                        (value as JSONObject).opt(
                            childValue as String,
                        )?.let {
                            handleJsonObject(
                                childValue,
                                it,
                                childItemView,
                                i < array.length() - 1,
                                hierarchy,
                            )
                        }
                    }
                    itemView.addViewNoInvalidate(childItemView)
                    i++
                }

                val childItemView = JsonItemView(itemView.context)
                childItemView.setTextSize(TEXT_SIZE_DP)
                childItemView.setRightColor(BRACES_COLOR)
                val builder: StringBuilder = StringBuilder((hierarchy - 1).getHierarchyStr())
                builder.append(if (isJsonArray) "]" else "}").append(if (appendComma) "," else "")
                childItemView.showRight(builder)
                itemView.addViewNoInvalidate(childItemView)
                itemView.requestLayout()
                itemView.invalidate()
            } else { // 折叠 <--> 展开
                val temp: CharSequence = itemView.rightText
                itemView.showRight(itemView.tag as CharSequence)
                itemView.tag = temp
                itemView.showIcon(!isCollapsed)
                for (i in 1 until itemView.childCount) {
                    itemView.getChildAt(i).visibility = if (isCollapsed) View.VISIBLE else View.GONE
                }
                isCollapsed = !isCollapsed
            }
        }
    }

    inner class JsonItemViewHolder(itemView: JsonItemView) : RecyclerView.ViewHolder(itemView) {
        var jsonItemView: JsonItemView? = null

        init {
            setIsRecyclable(false)
            this.jsonItemView = itemView
        }
    }
}
