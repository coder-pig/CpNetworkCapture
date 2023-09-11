package cn.coderpig.cpnetworkcapture

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.coderpig.cp_network_capture.interceptor.CaptureInterceptor
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val mClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(CaptureInterceptor())
            .build()
    }
    private lateinit var mContentTv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContentTv = findViewById(R.id.tv_content)
        findViewById<Button>(R.id.bt_send_request).setOnClickListener {
            sendRequest("https://www.wanandroid.com/article/list/0/json")
        }

    }

    private fun sendRequest(url: String) {
        mClient.newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { mContentTv.text = "${e.message}" }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread { Toast.makeText(this@MainActivity, "请求成功", Toast.LENGTH_SHORT).show() }
            }
        })
    }
}
