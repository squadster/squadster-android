package com.android.squadster.main.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

interface UserInfoView : MvpView {

    @OneExecution
    fun showLoading()

    @OneExecution
    fun hideLoading()

    @OneExecution
    fun showErrorMessage(error: String)
}