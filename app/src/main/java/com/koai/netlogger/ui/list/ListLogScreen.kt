package com.koai.netlogger.ui.list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.koai.base.main.extension.navigatorViewModel
import com.koai.base.main.extension.safeClick
import com.koai.base.main.extension.screenViewModel
import com.koai.base.main.screens.BaseScreen
import com.koai.netlogger.NetLogNavigator
import com.koai.netlogger.R
import com.koai.netlogger.databinding.ScreenListLogBinding

class ListLogScreen : BaseScreen<ScreenListLogBinding, ListLogRouter, NetLogNavigator>(R.layout.screen_list_log) {
    override val navigator: NetLogNavigator by navigatorViewModel()
    override val viewModel: ListLogViewModel by screenViewModel()
    private val adapter = ItemAdapter{
        router?.gotoDetailLog(it)
    }

    override fun initView(savedInstanceState: Bundle?, binding: ScreenListLogBinding) {
        binding.apply {
            btnClose.safeClick {
                activity.finish()
            }
            clearBtn.safeClick {
                viewModel.clearLog()
            }
            search.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(editable: Editable?) {
                    editable?.let {
                        viewModel.setSearchQuery(it.toString().trim())
                    }
                }
            })
            viewModel.getSearchedLogs().observe(viewLifecycleOwner) { items ->
                adapter.submitList(items.toMutableList())
            }
            list.adapter = adapter
            viewModel.items().observe(viewLifecycleOwner){
                adapter.submitList(it)
            }
        }

    }
}