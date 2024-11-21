package com.koai.netlogger.ui.list

import android.os.Bundle
import com.koai.base.main.extension.navigatorViewModel
import com.koai.base.main.screens.BaseScreen
import com.koai.netlogger.NetLogNavigator
import com.koai.netlogger.R
import com.koai.netlogger.databinding.ScreenListLogBinding

class ListLogScreen : BaseScreen<ScreenListLogBinding, ListLogRouter, NetLogNavigator>(R.layout.screen_list_log) {
    override val navigator: NetLogNavigator by navigatorViewModel()

    override fun initView(savedInstanceState: Bundle?, binding: ScreenListLogBinding) {

    }
}