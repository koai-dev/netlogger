package com.koai.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.koai.base.main.extension.safeClick
import com.koai.base.widgets.MoveImageView
import com.koai.netlogger.NetLoggerActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.location.observe(this){location->
            location?.let {
                findViewById<TextView>(R.id.textView4).text = it.toString()
            }?:run {
                findViewById<TextView>(R.id.textView4).text = "No data, \nHello world :v"
            }
        }
        findViewById<TextView>(R.id.button).safeClick {
            viewModel.getLocation()
        }
        findViewById<Button>(R.id.btn_clear).safeClick {
            viewModel.clearLog()
        }

        findViewById<MoveImageView>(R.id.btn_logger).safeClick {
            startActivity(Intent(this, NetLoggerActivity::class.java))
        }
    }
}