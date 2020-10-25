package com.android.squadster.ui.main.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.android.squadster.BuildConfig
import com.android.squadster.R
import com.android.squadster.core.BaseFragment
import com.android.squadster.main.main.UserInfoPresenter
import com.android.squadster.main.main.UserInfoView
import kotlinx.android.synthetic.main.fragment_user_info.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Scope

class UserInfoFragment : BaseFragment(), UserInfoView {

    override val layoutRes = R.layout.fragment_user_info

    override fun installScopeModules(scope: Scope) {
    }

    @InjectPresenter
    lateinit var userInfoPresenter: UserInfoPresenter

    @ProvidePresenter
    fun providePresenter(): UserInfoPresenter =
            scope.getInstance(UserInfoPresenter::class.java)

    override fun onBackPressed() {
        userInfoPresenter.onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    override fun showLoading() {
        clpb.visibility = View.VISIBLE
        clpb.show()
    }

    override fun hideLoading() {
        clpb.visibility = View.INVISIBLE
        clpb.hide()
    }

    override fun showErrorMessage(error: String) {
        txt_error.visibility = View.VISIBLE
        txt_error.text = error
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        webview.settings.domStorageEnabled = true
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                webview.animate().alpha(1.0f).duration = 500
                if (url != null) {
                    if (userInfoPresenter.getInfoAboutVKAccount(url)) {
                        webview.visibility = View.GONE
                        userInfoPresenter.getFullVKProfile()
                    }
                }
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)

                webview.visibility = View.GONE
                showErrorMessage("Something went wrong. Please restart app")
            }
        }

        webview.loadUrl("https://api.vkontakte.ru/oauth/authorize?" +
                "client_id=${BuildConfig.CLIENT_ID}&" +
                "redirect_uri=https://oauth.vk.com/blank.html&" +
                "response_type=token&" +
                "scope=email&" +
                "lang=en&" +
                "v=5.103")
    }
}