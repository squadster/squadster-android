package com.android.squadster.screenslogic.squadsettings

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.OneExecution

interface SquadSettingsView : MvpView {

    @OneExecution
    fun showErrorMessage(message: String)

    @AddToEnd
    fun updateRequest(id: String)

    @AddToEnd
    fun deleteSquad()
}