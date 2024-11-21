package com.koai.netlogger.ui.list

import com.koai.base.main.action.router.BaseRouter
import com.koai.netlogger.model.NetLogItem

interface ListLogRouter : BaseRouter {
    fun gotoDetailLog(item: NetLogItem)
}