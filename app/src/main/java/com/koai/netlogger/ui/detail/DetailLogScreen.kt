package com.koai.netlogger.ui.detail

import android.os.Bundle
import androidx.core.view.isVisible
import com.koai.base.main.extension.navigatorViewModel
import com.koai.base.main.extension.safeClick
import com.koai.base.main.screens.BaseScreen
import com.koai.netlogger.NetLogNavigator
import com.koai.netlogger.R
import com.koai.netlogger.databinding.ScreenDetailLogBinding
import com.koai.netlogger.model.NetLogItem
import com.koai.netlogger.utils.copyToClipboard
import com.koai.netlogger.utils.getSafeParcelable
import com.koai.netlogger.utils.requestBodyToJson

class DetailLogScreen :
    BaseScreen<ScreenDetailLogBinding, DetailLogRouter, NetLogNavigator>(R.layout.screen_detail_log) {
    override val navigator: NetLogNavigator by navigatorViewModel()

    override fun initView(savedInstanceState: Bundle?, binding: ScreenDetailLogBinding) {
        val data = arguments?.getSafeParcelable("logItem", NetLogItem::class.java)
        var head = ""
        data?.request?.headers?.forEach { (type, value) ->
            head = "$head \n$type : $value"
        }
        with(binding) {
            url.text = data?.url
            method.text = data?.request?.method
            header.text = head
            code.text = data?.response?.code.toString()
            data?.request?.body?.requestBodyToJson()?.let { body ->
                rvJsonBodyRequest.bindJson(body)
            }
            data?.responseBody?.let {
                rvJsonResponse.bindJson(it)
                bodyText.text = it
            }
            btnBack.safeClick {
                router?.onPopScreen()
            }
            btnCopyUrl.safeClick {
                activity.copyToClipboard(data?.url.toString())
            }
            btnCopyResponse.safeClick {
                activity.copyToClipboard(data?.responseBody.toString())
            }
            txtBody.safeClick {
                rvJsonBodyRequest.isVisible = !rvJsonBodyRequest.isVisible
                bodyText.isVisible = !bodyText.isVisible
                txtBody.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    if (rvJsonBodyRequest.isVisible) R.drawable.ic_up else R.drawable.ic_down,
                    0)
            }
        }
    }
}