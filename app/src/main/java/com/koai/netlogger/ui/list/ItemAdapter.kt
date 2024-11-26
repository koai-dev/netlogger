package com.koai.netlogger.ui.list

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import com.koai.base.main.adapter.BaseListAdapter
import com.koai.base.main.extension.safeClick
import com.koai.netlogger.R
import com.koai.netlogger.databinding.ItemNetlogBinding
import com.koai.netlogger.model.NetLogItem
import com.koai.netlogger.utils.format
import java.util.Date

class ItemAdapter(private val onClick: (NetLogItem)->Unit): BaseListAdapter<NetLogItem, ItemNetlogBinding>() {
    override fun bindView(holder: VH, binding: ItemNetlogBinding, position: Int) {
        val data = getItem(position)
        binding.mainItemNet.safeClick {
            onClick.invoke(data)
        }

        binding.url.text = data.url
        binding.method.text = data.request?.method
        binding.type.text = data.response?.headers?.get("content-Type")
        data.response?.let { setCodeColor(it.code, binding) }
        data.response?.let { setTimestamp(it.receivedResponseAtMillis, binding) }
        setDelayBetweenRequestAndResponse(data, binding)
        binding.executePendingBindings()
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun setDelayBetweenRequestAndResponse(item: NetLogItem, binding: ItemNetlogBinding) {
        val sendTime = item.response?.sentRequestAtMillis ?: 0
        val receivedTime = item.response?.receivedResponseAtMillis ?: 0
        val differTime = (receivedTime - sendTime).toFloat() / 1000f
        binding.delay.text = String.format("%.2f", differTime) + " s"
    }

    private fun setTimestamp(millis: Long, binding: ItemNetlogBinding) {
        binding.timestamp.text = Date(millis).format("HH:mm")
    }

    private fun setCodeColor(code: Int, binding: ItemNetlogBinding) {
        val color =  if (code == 200) {
            ContextCompat.getColor(binding.root.context.applicationContext, android.R.color.holo_green_light)
        } else {
            ContextCompat.getColor(binding.root.context.applicationContext, android.R.color.holo_red_light)
        }
        binding.codeColor.setBackgroundColor(color)
    }

    override fun getLayoutId(viewType: Int) = R.layout.item_netlog
}