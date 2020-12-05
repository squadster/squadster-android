package com.android.squadster.screenslogic.squads

import com.android.squadster.model.data.server.model.RequestStatus
import com.android.squadster.model.data.server.model.Squad
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.OneExecution

interface SquadsView : MvpView {

    @AddToEnd
    fun setSquads(squads: List<Squad>)

    @OneExecution
    fun showErrorMessage(message: String)

    @AddToEnd
    fun updateSquadInvitation(squadId: String, requestId: String, requestStatus: RequestStatus)
}