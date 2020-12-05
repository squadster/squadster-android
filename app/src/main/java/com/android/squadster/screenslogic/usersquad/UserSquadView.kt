package com.android.squadster.screenslogic.usersquad

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.OneExecution

interface UserSquadView : MvpView {

    @OneExecution
    fun showErrorMessage(message: String)

    @AddToEnd
    fun deleteSquadMember(id: String, role: String)

    @AddToEnd
    fun updateSquadMemberRole(id: String, oldRole: String, newRole: String, quequeNumber: Int)
}