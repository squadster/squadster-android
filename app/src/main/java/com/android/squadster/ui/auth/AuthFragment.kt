package com.android.squadster.ui.auth

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.android.squadster.R
import com.android.squadster.core.BaseFragment
import com.android.squadster.main.auth.AuthPresenter
import com.android.squadster.main.auth.AuthView
import kotlinx.android.synthetic.main.fragment_auth.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Scope

class AuthFragment : BaseFragment(), AuthView {

    override val layoutRes = R.layout.fragment_auth

    override fun installScopeModules(scope: Scope) {
    }

    @InjectPresenter
    lateinit var authPresenter: AuthPresenter

    @ProvidePresenter
    fun providePresenter(): AuthPresenter =
        scope.getInstance(AuthPresenter::class.java)

    override fun onBackPressed() {
        authPresenter.onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun showLoading() {
        clpb.visibility = View.VISIBLE
        clpb.show()
    }

    private fun hideLoading() {
        clpb.visibility = View.INVISIBLE
        clpb.hide()
    }

    private fun showErrorMessage() {
        txt_error.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        txt_error.visibility = View.GONE
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        webview.settings.domStorageEnabled = true
        webview.settings.javaScriptEnabled = true
        webview.addJavascriptInterface(MyJavaScriptInterface(), "HtmlViewer")
        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                hideErrorMessage()
                showLoading()
                if (url != null && url.contains(CALLBACK_URL)) {
                    webview.visibility = View.GONE
                } else {
                    webview.visibility = View.VISIBLE
                }
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                hideLoading()
                webview.animate().alpha(1.0f).duration = 500
                if (url != null && url.contains(CALLBACK_URL)) {
                    webview.loadUrl(JAVASCRIPT_COMMAND)
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                hideLoading()
                webview.visibility = View.GONE
                showErrorMessage()
            }
        }

        webview.loadUrl(AUTH_URL)
    }

    inner class MyJavaScriptInterface {

        @JavascriptInterface
        fun getInfo(content: String) {
            activity?.runOnUiThread {
                authPresenter.getAllInfoAboutUser(content)
            }
        }
    }

    companion object {
        private const val AUTH_URL = "http://squadster.wtf/api/auth/vk?state=mobile=true"
        private const val CALLBACK_URL = "http://squadster.wtf/api/auth/vk/callback?code="
        private const val JAVASCRIPT_COMMAND =
            "javascript:window.HtmlViewer.getInfo('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');"
    }
}