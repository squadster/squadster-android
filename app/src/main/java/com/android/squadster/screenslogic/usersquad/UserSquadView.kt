package com.android.squadster.screenslogic.usersquad

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.OneExecution

interface UserSquadView : MvpView {

    @OneExecution
    fun showErrorMessage(message: String)

    @AddToEnd
    fun deleteSquadMember(id: String)

    @AddToEnd
    fun updateSquadMemberRole(id: String, role: String, quequeNumber: Int)
}