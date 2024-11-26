package com.koai.netlogger

import android.graphics.Rect
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.koai.base.main.BaseActivity
import com.koai.base.main.action.router.BaseRouter
import com.koai.base.utils.LogUtils
import com.koai.netlogger.databinding.ActivityNetLoggerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NetLoggerActivity : BaseActivity<ActivityNetLoggerBinding, BaseRouter, NetLogNavigator>(R.layout.activity_net_logger) {
    override val navigator: NetLogNavigator by viewModel()

    override fun initView(savedInstanceState: Bundle?, binding: ActivityNetLoggerBinding) {
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            statusBarHeight = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            try {
                val layoutParamPointTop =
                    binding.pointTop.layoutParams as ViewGroup.MarginLayoutParams
                layoutParamPointTop.topMargin = statusBarHeight
                binding.pointTop.layoutParams = layoutParamPointTop

                val layoutParamPointBot =
                    binding.pointBot.layoutParams as ViewGroup.MarginLayoutParams
                layoutParamPointBot.bottomMargin = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                binding.pointBot.layoutParams = layoutParamPointBot
            } catch (e: Exception) {
                LogUtils.log("Error in Margin", e.message ?: "Unknown Error!")
            }
            ViewCompat.onApplyWindowInsets(view, windowInsets)
        }
        handleKeyBoardVisible()
    }

    private fun handleKeyBoardVisible() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is visible, add padding to the bottom of the layout
                binding.root.rootView.setPadding(0, 0, 0, keypadHeight)
            } else {
                // Keyboard is hidden, reset padding
                binding.root.rootView.setPadding(0, 0, 0, 0)
            }
        }
    }

}