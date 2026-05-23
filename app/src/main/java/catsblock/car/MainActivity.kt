package com.catsblock.car

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val container = FrameLayout(this)
        val webView = WebView(this)
        container.addView(webView)
        setContentView(container)

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.setGeolocationEnabled(true)
        
        // 1. MUDANÇA CRÍTICA: Fazer a WebView fingir ser o Chrome Desktop/Mobile
        // Isso engana o Google para evitar o erro 403
        settings.userAgentString = "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36"

        // 2. Garante persistência dos cookies
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.webViewClient = WebViewClient()
        
        webView.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: android.os.Message?): Boolean {
                val newWebView = WebView(this@MainActivity)
                newWebView.settings.javaScriptEnabled = true
                newWebView.settings.userAgentString = settings.userAgentString // O popup também precisa fingir ser o Chrome
                container.addView(newWebView)
                
                val transport = resultMsg?.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                
                return true
            }
        }

        webView.loadUrl("https://catsblock-car.pages.dev")
    }

    // 3. Garante que os cookies sejam salvos permanentemente no disco do aparelho
    override fun onPause() {
        super.onPause()
        CookieManager.getInstance().flush()
    }
}
