package com.koai.netlogger.ui.detail

import android.os.Bundle
import com.koai.base.main.extension.navigatorViewModel
import com.koai.base.main.screens.BaseScreen
import com.koai.netlogger.NetLogNavigator
import com.koai.netlogger.R
import com.koai.netlogger.databinding.ScreenDetailLogBinding

class DetailLogScreen : BaseScreen<ScreenDetailLogBinding, DetailLogRouter, NetLogNavigator>(R.layout.screen_detail_log){
    override val navigator: NetLogNavigator by navigatorViewModel()

    override fun initView(savedInstanceState: Bundle?, binding: ScreenDetailLogBinding) {

    }
}