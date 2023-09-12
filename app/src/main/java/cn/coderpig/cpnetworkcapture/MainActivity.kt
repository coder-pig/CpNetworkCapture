package cn.coderpig.cpnetworkcapture

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val getUrlList = arrayListOf(
        "https://www.wanandroid.com/article/list/0/json",
        "https://www.wanandroid.com/banner/json",
        "https://www.wanandroid.com/friend/json",
        "https://www.wanandroid.com//hotkey/json",
        "https://www.wanandroid.com/article/top/json",
        "https://www.wanandroid.com/tree/json",
        "https://www.wanandroid.com/tree/json",
        "https://www.wanandroid.com/navi/json",
        "https://www.wanandroid.com/project/tree/json"
    )
    private val postUrl = "https://www.wanandroid.com/user/login"

    private val mClient by lazy {
        var captureInterceptor: Interceptor? = null
        try {
            val clazz = Class.forName("cn.coderpig.cp_network_capture.interceptor.CaptureInterceptor")
            val constructor = clazz.getDeclaredConstructor()
            captureInterceptor = constructor.newInstance() as Interceptor
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val builder = OkHttpClient.Builder()
        captureInterceptor?.let { builder.addInterceptor(it) }
        builder.build()
    }
    private lateinit var mContentTv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContentTv = findViewById(R.id.tv_content)
        findViewById<Button>(R.id.bt_send_get_request).setOnClickListener {
            sendGetRequest()
        }
        findViewById<Button>(R.id.bt_send_post_request).setOnClickListener {
            sendPostRequest()
        }
    }

    private fun sendGetRequest() {
        getUrlList.random().let {
            mClient.newCall(Request.Builder().url(it).build()).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    refreshContentUI("请求：$it 失败：${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    refreshContentUI("请求：$it 成功")
                }
            })
        }
    }

    private fun sendPostRequest() {
        val requestBody: RequestBody = FormBody.Builder()
            .add("username", "123")
            .add("password", "321")
            .build()
        mClient.newCall(Request.Builder().url(postUrl).post(requestBody).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                refreshContentUI("请求：$postUrl 失败：${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                refreshContentUI("请求：$postUrl 成功")
            }
        })
    }

    private fun refreshContentUI(text: String) {
        runOnUiThread { mContentTv.text = "${mContentTv.text}\n${text}" }
    }

}
