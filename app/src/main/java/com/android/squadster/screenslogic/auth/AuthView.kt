package com.android.squadster.screenslogic.auth

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.OneExecution

interface AuthView : MvpView {

    @OneExecution
    fun showErrorMessage(message: String)
}