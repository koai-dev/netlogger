package com.koai.netlogger

import android.os.Bundle
import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.netlogger.model.NetLogItem
import com.koai.netlogger.ui.detail.DetailLogRouter
import com.koai.netlogger.ui.list.ListLogRouter

class NetLogNavigator: BaseNavigator(), ListLogRouter, DetailLogRouter {
    override fun gotoDetailLog(item: NetLogItem) {
        offNavScreen(action = R.id.action_global_detailLogScreen, extras = Bundle().apply {
            putParcelable("logItem", item)
        })
    }
}