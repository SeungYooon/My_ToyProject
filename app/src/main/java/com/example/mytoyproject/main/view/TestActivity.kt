package com.example.mytoyproject.main.view

import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.*
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import com.example.mytoyproject.R
import com.example.mytoyproject.databinding.ActivityTestBinding
import kotlinx.android.synthetic.main.activity_test.*
import java.lang.IllegalArgumentException

class TestActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL = "https://github.com/"
        const val URL = "url"
        const val MAX_PROGRESS = 100
    }

    private lateinit var binding: ActivityTestBinding

    private val url: String by lazy {
        intent.getStringExtra(URL) ?: throw IllegalArgumentException("$URL is null")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test)

        initWebView()
        setWebClient()
        loadUrl(BASE_URL + url)
    }

    private fun initWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()
            }
        }
    }

    private fun setWebClient() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress < MAX_PROGRESS && progressBar.visibility == ProgressBar.GONE) {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
                if (newProgress == MAX_PROGRESS) {
                    progressBar.visibility = ProgressBar.GONE
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun loadUrl(url: String) {
        webView.loadUrl(url)
    }
}
