package com.koai.netlogger

import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.netlogger.ui.detail.DetailLogRouter
import com.koai.netlogger.ui.list.ListLogRouter

class NetLogNavigator : BaseNavigator(), ListLogRouter, DetailLogRouter {
    override fun gotoDetailLog() {
        offNavScreen(
            action = R.id.action_global_detailLogScreen,
        )
    }
}
