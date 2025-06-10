package com.koai.netlogger.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.koai.base.core.ui.extension.safeClick
import com.koai.base.core.ui.extension.withSafeContext
import com.koai.base.core.ui.screens.BaseScreen
import com.koai.base.di.journeyViewModel
import com.koai.base.di.navigatorViewModel
import com.koai.netlogger.NetLogNavigator
import com.koai.netlogger.R
import com.koai.netlogger.databinding.ScreenDetailLogBinding
import com.koai.netlogger.ui.list.ListLogViewModel
import com.koai.netlogger.utils.copyToClipboard
import com.koai.netlogger.utils.requestBodyToJson

class DetailLogScreen :
    BaseScreen<ScreenDetailLogBinding, DetailLogRouter, NetLogNavigator>(R.layout.screen_detail_log) {
    override val navigator: NetLogNavigator by navigatorViewModel()
    override val viewModel: ListLogViewModel by journeyViewModel()

    override fun initView(
        savedInstanceState: Bundle?,
        binding: ScreenDetailLogBinding,
    ) {
        viewModel.currentItemNetLog.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                var head = ""
                data.request?.headers?.forEach { (type, value) ->
                    head = "$head \n$type : $value"
                }
                withSafeContext {activity ->
                    with(binding) {
                        try {
                            url.text = data.url
                            method.text = data.request?.method
                            header.text = head
                            header.isVisible = head.isNotEmpty()
                            textView2.isVisible = head.isNotEmpty()
                            code.text = data.response?.code.toString()
                            if (data.response?.code == 200) {
                                code.setTextColor(activity.getColor(R.color.green))
                                method.setTextColor(activity.getColor(R.color.green))
                                url.setTextColor(activity.getColor(R.color.green))
                            } else {
                                method.setTextColor(activity.getColor(R.color.red))
                                url.setTextColor(activity.getColor(R.color.red))
                                code.setTextColor(activity.getColor(R.color.red))
                            }
                            data.request?.body?.requestBodyToJson()?.let { body ->
                                ctnBody.bindJson(body)
                                ctnBody.expandAll()
                                bodyText.text = body
                            } ?: kotlin.run {
                                ctnBody.isVisible = false
                                txtBody.isVisible = false
                                bodyText.isVisible = false
                            }
                            data.responseBody.let { body ->
                                rvJsonResponse.bindJson(body)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                            activity.navController?.popBackStack()
                        }
                        btnCopyUrl.safeClick {
                            activity.copyToClipboard(data.url)
                            Toast.makeText(activity, "Url Copied", Toast.LENGTH_SHORT).show()
                        }
                        btnCopyResponse.safeClick {
                            activity.copyToClipboard(data.responseBody)
                            Toast.makeText(activity, "Response Copied", Toast.LENGTH_SHORT).show()
                        }
                        btnCopyHeader.safeClick {
                            activity.copyToClipboard(header.text.toString())
                            Toast.makeText(activity, "Header Copied", Toast.LENGTH_SHORT).show()
                        }
                        btnCopyBodyRequest.safeClick {
                            activity.copyToClipboard(bodyText.text.toString())
                            Toast.makeText(activity, "Body Request Copied", Toast.LENGTH_SHORT).show()
                        }

                        btnBack.safeClick {
                            activity.navController?.popBackStack()
                        }
                        txtBody.safeClick {
                            ctnBody.isVisible = !ctnBody.isVisible
                            bodyText.isVisible = !bodyText.isVisible
                        }
                    }
                }
            }
        }
    }
}
