package com.catsblock.car

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Mantém a imersão escondendo a barra do sistema
        supportActionBar?.hide()

        val webView = WebView(this)
        setContentView(webView)

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true // Essencial para o LocalStorage de Viagens
        settings.setGeolocationEnabled(true) // Essencial para rastreio de navegação
        settings.databaseEnabled = true
        
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                // Força aprovação de localização via wrapper nativo para HTML
                callback.invoke(origin, true, false)
            }
        }

        // Lê seu código HTML perfeitamente alocado na pasta assets
        webView.loadUrl("file:///android_asset/index.html")
    }
}
