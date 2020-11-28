package com.android.squadster.screenslogic.squads

import com.android.squadster.model.data.server.model.RequestStatus
import com.squadster.server.GetSquadsQuery
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.OneExecution

interface SquadsView : MvpView {

    @AddToEnd
    fun setSquads(squads: List<GetSquadsQuery.Squad>)

    @OneExecution
    fun showErrorMessage(message: String)

    @AddToEnd
    fun showEmptyListOfSquads()

    @AddToEnd
    fun updateSquadInvitation(squadId: String, requestId: String, requestStatus: RequestStatus)

    @AddToEnd
    fun goToUserSquad()
}