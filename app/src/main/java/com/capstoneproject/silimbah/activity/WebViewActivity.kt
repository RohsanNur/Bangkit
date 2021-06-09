package com.capstoneproject.silimbah.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.capstoneproject.silimbah.databinding.ActivityWebViewBinding
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    companion object{
        const val EXTRA_URL = "extra_url"
    }
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(EXTRA_URL)
        binding.apply {
            webView.loadUrl(url)
            webView.settings.javaScriptEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    view.loadUrl("javascript:alert('Web berhasil dimuat')")
                }
            }
            webView.webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(view: WebView, url: String, message: String, result: android.webkit.JsResult): Boolean {
                    Toast.makeText(this@WebViewActivity, message, Toast.LENGTH_LONG).show()
                    result.confirm()
                    return true
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}